package com.yapp.data.model

import com.yapp.data.model.AttendanceTypeModel.Companion.mapToEntity
import com.yapp.domain.model.AttendanceEntity
import com.yapp.domain.model.AttendanceTypeEntity
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class AttendanceModel(
    @SerialName("session_id")
    val sessionId: Int? = null,
    val type: AttendanceTypeModel? = null
) {

    companion object {
        fun AttendanceModel.mapToEntity(): AttendanceEntity {
            return AttendanceEntity(
                sessionId = this.sessionId!!,
                type = this.type!!.mapToEntity()
            )
        }
    }

}

@Serializable
data class AttendanceTypeModel(val point: Int = 0, val text: String = ABSENT) {

    companion object {
        private const val ABSENT = "미통보 결석"
        private const val REPORTED_ABSENT = "결석"
        private const val NORMAL = "출석"
        private const val LATE = "미통보 지각"
        private const val REPORTED_LATE = "지각"

        fun AttendanceTypeModel.mapToEntity(): AttendanceTypeEntity {
            return when (this.text) {
                ABSENT -> AttendanceTypeEntity.Absent
                REPORTED_ABSENT -> AttendanceTypeEntity.ReportedAbsent
                NORMAL -> AttendanceTypeEntity.Normal
                LATE -> AttendanceTypeEntity.Late
                REPORTED_LATE -> AttendanceTypeEntity.ReportedLate
                else -> error("알 수 없는 AttendanceType입니다.")
            }
        }
    }

}