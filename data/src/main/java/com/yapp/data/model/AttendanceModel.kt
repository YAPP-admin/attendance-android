package com.yapp.data.model

import com.yapp.data.Constants.ABSENT
import com.yapp.data.Constants.EMPTY
import com.yapp.data.Constants.LATE
import com.yapp.data.Constants.NORMAL
import com.yapp.data.Constants.REPORTED_ABSENT
import com.yapp.data.Constants.REPORTED_LATE
import com.yapp.data.model.AttendanceTypeModel.Companion.mapToEntity
import com.yapp.domain.model.AttendanceEntity
import com.yapp.domain.model.AttendanceTypeEntity
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class AttendanceModel(
    @SerialName("session_id")
    val sessionId: Int? = -1,
    val type: AttendanceTypeModel = (AttendanceTypeModel(0, "df"))
) {

    companion object {
        fun AttendanceModel.mapToEntity(): AttendanceEntity {
            return AttendanceEntity(
                sessionId = this.sessionId!!,
                type = this.type.mapToEntity()
            )
        }
    }

}

@Serializable
data class AttendanceTypeModel(val point: Int = 0, val text: String = "EMPTY") {

    companion object {
        fun AttendanceTypeModel.mapToEntity(): AttendanceTypeEntity {
            return when (this.text) {
                ABSENT -> AttendanceTypeEntity.Absent
                REPORTED_ABSENT -> AttendanceTypeEntity.ReportedAbsent
                NORMAL -> AttendanceTypeEntity.Normal
                LATE -> AttendanceTypeEntity.Late
                REPORTED_LATE -> AttendanceTypeEntity.ReportedLate
                EMPTY -> AttendanceTypeEntity.Empty
                else -> error("알수 없는 AttendanceType입니다.")
            }
        }
    }

}