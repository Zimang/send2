/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */


import org.apache.commons.compress.compressors.FileNameUtil
import java.nio.charset.Charset
import java.nio.charset.StandardCharsets

/**
 * Utility code for the gzip compression format.
 *
 * @ThreadSafe
 */
object GzipUtils {
    private val fileNameUtil: FileNameUtil

    init {
        // using LinkedHashMap so .tgz is preferred over .taz as
        // compressed extension of .tar as FileNameUtil will use the
        // first one found
        val uncompressSuffix: MutableMap<String, String> = LinkedHashMap()
        uncompressSuffix[".tgz"] = ".tar"
        uncompressSuffix[".taz"] = ".tar"
        uncompressSuffix[".svgz"] = ".svg"
        uncompressSuffix[".cpgz"] = ".cpio"
        uncompressSuffix[".wmz"] = ".wmf"
        uncompressSuffix[".emz"] = ".emf"
        uncompressSuffix[".gz"] = ""
        uncompressSuffix[".z"] = ""
        uncompressSuffix["-gz"] = ""
        uncompressSuffix["-z"] = ""
        uncompressSuffix["_z"] = ""
        fileNameUtil = FileNameUtil(uncompressSuffix, ".gz")
    }

    /**
     * Encoding for file name and comments per the [GZIP File Format Specification](https://tools.ietf.org/html/rfc1952)
     */
    val GZIP_ENCODING: Charset = StandardCharsets.ISO_8859_1

    /**
     * Maps the given file name to the name that the file should have after compression with gzip. Common file types with custom suffixes for compressed
     * versions are automatically detected and correctly mapped. For example the name "package.tar" is mapped to "package.tgz". If no custom mapping is
     * applicable, then the default ".gz" suffix is appended to the file name.
     *
     * @param fileName name of a file
     * @return name of the corresponding compressed file
     */
    @Deprecated("Use {@link #getCompressedFileName(String)}.")
    fun getCompressedFilename(fileName: String?): String {
        return fileNameUtil.getCompressedFileName(fileName)
    }

    /**
     * Maps the given file name to the name that the file should have after compression with gzip. Common file types with custom suffixes for compressed
     * versions are automatically detected and correctly mapped. For example the name "package.tar" is mapped to "package.tgz". If no custom mapping is
     * applicable, then the default ".gz" suffix is appended to the file name.
     *
     * @param fileName name of a file
     * @return name of the corresponding compressed file
     * @since 1.25.0
     */
    fun getCompressedFileName(fileName: String?): String {
        return fileNameUtil.getCompressedFileName(fileName)
    }

    /**
     * Maps the given name of a gzip-compressed file to the name that the file should have after uncompression. Commonly used file type specific suffixes like
     * ".tgz" or ".svgz" are automatically detected and correctly mapped. For example the name "package.tgz" is mapped to "package.tar". And any file names with
     * the generic ".gz" suffix (or any other generic gzip suffix) is mapped to a name without that suffix. If no gzip suffix is detected, then the file name is
     * returned unmapped.
     *
     * @param fileName name of a file
     * @return name of the corresponding uncompressed file
     */
    @Deprecated("Use {@link #getUncompressedFileName(String)}.")
    fun getUncompressedFilename(fileName: String?): String {
        return fileNameUtil.getUncompressedFileName(fileName)
    }

    /**
     * Maps the given name of a gzip-compressed file to the name that the file should have after uncompression. Commonly used file type specific suffixes like
     * ".tgz" or ".svgz" are automatically detected and correctly mapped. For example the name "package.tgz" is mapped to "package.tar". And any file names with
     * the generic ".gz" suffix (or any other generic gzip suffix) is mapped to a name without that suffix. If no gzip suffix is detected, then the file name is
     * returned unmapped.
     *
     * @param fileName name of a file
     * @return name of the corresponding uncompressed file
     * @since 1.25.0
     */
    fun getUncompressedFileName(fileName: String?): String {
        return fileNameUtil.getUncompressedFileName(fileName)
    }

    /**
     * Detects common gzip suffixes in the given file name.
     *
     * @param fileName name of a file
     * @return `true` if the file name has a common gzip suffix, `false` otherwise
     */
    @Deprecated("Use {@link #isCompressedFileName(String)}.")
    fun isCompressedFilename(fileName: String?): Boolean {
        return fileNameUtil.isCompressedFileName(fileName)
    }

    /**
     * Detects common gzip suffixes in the given file name.
     *
     * @param fileName name of a file
     * @return `true` if the file name has a common gzip suffix, `false` otherwise
     * @since 1.25.0
     */
    fun isCompressedFileName(fileName: String?): Boolean {
        return fileNameUtil.isCompressedFileName(fileName)
    }
}