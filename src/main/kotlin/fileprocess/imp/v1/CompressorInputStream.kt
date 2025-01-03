package org.example.fileprocess.imp.v1

import java.io.InputStream

abstract class CompressorInputStream
    : InputStream() {
    var bytesRead: Long = 0
        private set

    /**
     * Increments the counter of already read bytes.
     * Doesn't increment if the EOF has been hit (read == -1)
     */
    protected fun count(read: Int) {
        count(read.toLong())
    }

    /**
     * Increments the counter of already read bytes.
     * Doesn't increment if the EOF has been hit (read == -1)
     */
    protected fun count(read: Long) {
        if (read != -1L) {
            bytesRead += read
        }
    }


    val uncompressedCount: Long
        /**
         * Gets the amount of raw or compressed bytes read by the stream.
         * This implementation invokes [.getBytesRead].
         * Provides half of [org.apache.commons.compress.utils.InputStreamStatistics]
         * without forcing subclasses to implement the other half.
         * @return the amount of decompressed bytes returned by the stream
         */
        get() = bytesRead

    /**
     * Decrements the counter of already read bytes.
     * @param pushedBack the number of bytes pushed back.
     */
    protected fun pushedBackBytes(pushedBack: Long) {
        bytesRead -= pushedBack
    }
}
