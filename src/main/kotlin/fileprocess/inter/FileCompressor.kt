package org.example.fileprocess.inter

import java.io.File

interface FileCompressor {
    fun compressFile(file: File, targetPath: String): Boolean // 压缩文件
    fun decompressFile(file: File, targetPath: String): Boolean // 解压文件
    fun splitAndCompress(file: File, chunkSize: Long): List<File> // 分块压缩
    fun combineChunks(chunks: List<File>, targetPath: String): Boolean // 合并分块文件
}
