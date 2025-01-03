package org.example.fileprocess.imp.v1.gzip

import org.apache.commons.io.input.BoundedInputStream
import org.example.fileprocess.imp.v1.CompressorInputStream
import org.example.fileprocess.imp.v1.InputStreamStatistics
import java.io.ByteArrayOutputStream
import java.io.DataInput
import java.io.InputStream
import java.security.Signature
import kotlin.jvm.Throws

class GzipCompressorInputStream(
    inputStream: InputStream,
    decompressConcatenated:Boolean=false
):CompressorInputStream(),InputStreamStatistics {

    companion object{
        private const val FHCRC: Int = 0x02
        private const val FEXTRA: Int = 0x04
        private const val FNAME: Int = 0x08
        private const val FCOMMENT: Int = 0x10
        private const val FRESERVED: Int = 0xE0

        fun matches(signature: ByteArray,length:Int):Boolean=length>=2&&signature[0].toInt()==31&&signature[1].toInt()==-117

        private fun readToNull(inData:DataInput):ByteArray{
            return ByteArrayOutputStream().use {
                var b:Int
                while ((inData.readUnsignedByte().also { b=it })!=0)
                    it.write(b)
                it.toByteArray()
            }
        }
    }


    private val countingStream:BoundedInputStream=BoundedInputStream.builder()
        .setInputStream(inputStream)
        .get()

    init {
        if (countingStream.markSupported()){

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
}