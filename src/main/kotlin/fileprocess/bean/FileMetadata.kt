package org.example.fileprocess.bean

import java.util.UUID
import kotlin.uuid.Uuid

data class FileMetadata(
    val fileName:String,
    val fileSize:Long,
    val filePath:String,
    val fileType:String,
    val uuid: UUID,

    val fileStatus:FILE_STATUS=FILE_STATUS.UNKNOWN

)
enum class FILE_STATUS {
    SHARED,
    PENDING,
    UNKNOWN
}