package org.example.fileprocess.inter

import org.example.fileprocess.bean.FileMetadata
import java.io.File

interface FileOperator {
    fun openFile(filePath: String): File?        // 打开文件
    fun closeFile(file: File)                    // 关闭文件
    fun readFile(file: File): ByteArray?         // 读取文件内容
    fun writeFile(file: File, data: ByteArray)   // 写入文件内容
    fun deleteFile(file: File): Boolean          // 删除文件
    fun renameFile(file: File, newName: String): Boolean // 重命名文件
    fun getFileMetadata(file: File): FileMetadata // 获取文件元数据
}