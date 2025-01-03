package org.example.fileprocess.imp.v1.utils

import org.apache.commons.compress.compressors.gzip.GzipCompressorInputStream
import java.io.BufferedInputStream
import java.nio.file.Files
import java.nio.file.Paths


class ApacheUtils {
    fun fu1(buffersize:Int){
        val fin = Files.newInputStream(Paths.get("archive.tar.gz"))
        val `in` = BufferedInputStream(fin)
        val out = Files.newOutputStream(Paths.get("archive.tar"))
        val gzIn = GzipCompressorInputStream(`in`)
        val buffer = ByteArray(buffersize)
        var n = 0
        while (-1 != (gzIn.read(buffer).also { n = it })) {
            out.write(buffer, 0, n)
        }
        out.close()
        gzIn.close()
    }
}