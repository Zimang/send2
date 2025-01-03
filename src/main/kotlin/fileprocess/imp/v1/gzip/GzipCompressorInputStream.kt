package org.example.fileprocess.imp.v1.gzip

import GzipUtils
import org.apache.commons.compress.compressors.gzip.GzipCompressorInputStream
import org.apache.commons.compress.compressors.gzip.GzipParameters
import org.apache.commons.compress.utils.ByteUtils
import org.apache.commons.io.input.BoundedInputStream
import org.example.fileprocess.imp.v1.CompressorInputStream
import org.example.fileprocess.imp.v1.InputStreamStatistics
import java.io.*
import java.util.zip.CRC32
import java.util.zip.Deflater
import java.util.zip.Inflater

class GzipCompressorInputStream(
    private val inputStream: InputStream,
    private val decompressConcatenated:Boolean=false
):CompressorInputStream(),InputStreamStatistics {


    private val countingStream=BoundedInputStream.builder()
        .setInputStream(inputStream)
        .get()

    private val _in:InputStream
    private val buf=ByteArray(8192)

    private var bufUsed: Int = 0
    private var endReached  = false
    //解压器
    private val inf=Inflater(true)
    //crc32 来自未压缩的数据
    private val crc=CRC32()
    //用于无参read方法
    private val ponByte=ByteArray(1)
    var parameter=GzipParameters()
        private set



    init {
        if (countingStream.markSupported()){
            _in=countingStream
        }else{
            _in=BufferedInputStream(countingStream)
        }

    }

    companion object{
        private const val FHCRC: Int = 0x02
        private const val FEXTRA: Int = 0x04
        private const val FNAME: Int = 0x08
        private const val FCOMMENT: Int = 0x10
        private const val FRESERVED: Int = 0xE0

        fun matches(signature: ByteArray,length:Int):Boolean=length>=2&&signature[0].toInt()==31&&signature[1].toInt()==-117

        @Throws(IOException::class)
        private fun readToNull(inData:DataInput):ByteArray{
            return try {
                    ByteArrayOutputStream().use {
                    var b:Int
                    while ((inData.readUnsignedByte().also { b=it })!=0)
                        it.write(b)
                    it.toByteArray()
                }
            } catch (e:IOException) {
                //TODO 打印日志
                ByteArray(0)
            }
        }
    }



    override fun read(): Int {
        TODO("Not yet implemented")
    }

    override fun getCompressedCount(): Long {
        TODO("Not yet implemented")
    }

    override fun getUnCompressedCount(): Long {
        TODO("Not yet implemented")
    }

    private fun _init(isFirstMember:Boolean):Boolean{
        //如果不是第一个成员那么就说明当前在加压第二个开外的成员，
        //也就是说明当前应该是支持解压多个成员的
        if (!isFirstMember&&!decompressConcatenated)
            throw IllegalArgumentException("Unexpected: isFirstMember and decompressConcatenated are both false, at least one must be true")

        val magic0=_in.read()

        //magic0 == -1 表示输入流已经到达了末尾，没有更多的字节可以读取。
        //如果当前不是第一个成员，那么就是支持解压多个文件，说明解压正常结束，可以退出
        //为什么_init要有这一段逻辑呢？
        if (magic0==-1&&!isFirstMember){
            return false
        }

        //如果第一个字节不等于 31,第二个字节不等于 139，说明输入流的内容并不是一个合法的 .gz 文件。
        //如果是第一个成员,说明输入流的内容根本不是一个合法的 .gz 文件。
        //如果不是第一个成员,说明在解压完一个合法的 .gz 成员后，流中包含了无效的垃圾数据。
        if (magic0 != 31 || _in.read() != 139) {
            throw IOException(if (isFirstMember) "Input is not in the .gz format" else "Garbage after a valid .gz stream")
        }

        //将输入流 _in 包装成 DataInputStream，以便可以使用更高级的读取方法（例如 readUnsignedByte）
        val inData=DataInputStream(_in)
        val method=inData.readUnsignedByte()
        //8（Deflater.DEFLATED）：表示文件使用了 DEFLATE 压缩算法，这是 .gz 格式的唯一合法压缩方法。
        if (method!=Deflater.DEFLATED){
            throw IOException("Unsupported compression method $method in the .gz header")
        }

        //flg:这是一个 8 位的标志字节，用来表示一些额外的信息或选项。
        //对标志字节 flg 和保留位掩码 FRESERVED 执行按位与操作。
        //如果结果不等于 0，说明保留位被设置了，违反 .gz 格式规范。
        val flg = inData.readUnsignedByte()
        if ((flg and FRESERVED) != 0) {
            throw IOException("Reserved flags are set in the .gz header")
        }

        //读取 .gz 文件头部的修改时间（modificationTime）并将其设置到 parameters 对象中。
            //从 inData 中以小端字节顺序读取 4 字节的数据，并将其转换为一个整数（Unix 时间戳）。
            //由于这个时间戳是以秒为单位的，乘以 1000 将其转换为毫秒为单位的时间。
        parameter.modificationTime = ByteUtils.fromLittleEndian(inData as DataInput, 4) * 1000

        //这行代码读取一个字节，表示 "extra flags"（额外标志位）。这些标志位通常用于指定压缩级别或其他附加信息。
        when (inData.readUnsignedByte()) {
            2 -> parameter.setCompressionLevel(Deflater.BEST_COMPRESSION)
            4 -> parameter.setCompressionLevel(Deflater.BEST_SPEED)
            else -> {}
        }
        //读取一个无符号字节，表示文件创建时使用的操作系统类型（例如 Unix、Windows、VMS 等）。
        parameter.operatingSystem = inData.readUnsignedByte()


        // Extra field, ignored
        if ((flg and FEXTRA) != 0) {
            var xlen = inData.readUnsignedByte()
            xlen = xlen or (inData.readUnsignedByte() shl 8)

            // 尽管当前的处理方法比使用 in.skip() 效率低，
            // 但它更简单且足够有效，因为大多数文件都不包含额外字段，
            // 所以这种方式是可接受的。
            repeat(xlen){
                inData.readUnsignedByte()
            }
        }

        if (flg and FNAME!=0)  parameter.fileName=String(readToNull(inData), GzipUtils.GZIP_ENCODING)

        if (flg and FCOMMENT!=0) parameter.comment=String(readToNull(inData),GzipUtils.GZIP_ENCODING)

        if (flg and FHCRC!=0) inData.readShort()

        //这两个 reset() 操作保证了多个 .gz 文件或多个数据块的处理不会相互干扰，
        // 并且每个文件或数据块的解压缩和校验都是独立的。
        inf.reset()
        crc.reset()
        return true
    }

    @Throws(IOException::class)
    override fun read(b: ByteArray, off: Int, len: Int): Int {
        if (len==0) return 0
        if (endReached) return -1

        val size=0
        repeat(len){
            if (inf.needsInput())
        }
    }
}