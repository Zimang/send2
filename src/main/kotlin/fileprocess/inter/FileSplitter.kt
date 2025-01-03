package org.example.fileprocess.inter

import java.io.File

interface FileSplitter {
    fun splitFile(file: File, chunkSize: Long): List<File> // 分割文件
    fun combineChunks(chunks: List<File>, targetPath: String): Boolean // 合并文件
}
