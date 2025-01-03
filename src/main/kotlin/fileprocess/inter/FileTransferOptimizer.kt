package org.example.fileprocess.inter

import java.io.File

interface FileTransferOptimizer {
    fun optimizeUpload(file: File, destination: String): Boolean // 优化上传
    fun optimizeDownload(file: File, source: String): Boolean // 优化下载
}
