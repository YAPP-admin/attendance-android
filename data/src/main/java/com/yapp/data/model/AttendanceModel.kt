package com.yapp.data.model

import com.yapp.data.model.AttendanceTypeModel.Companion.mapToEntity
import com.yapp.domain.model.AttendanceEntity
import com.yapp.domain.model.AttendanceTypeEntity
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class AttendanceModel(
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
data class AttendanceTypeModel(val point: Int = -20, val text: String = ABSENT) {

    companion object {
        private const val ABSENT = "결석"
        private const val NORMAL = "출석"
        private const val LATE = "지각"
        private const val ADMIT = "출석 인정"

        fun AttendanceTypeModel.mapToEntity(): AttendanceTypeEntity {
            return when (this.text) {
                ABSENT -> AttendanceTypeEntity.Absent
                NORMAL -> AttendanceTypeEntity.Normal
                LATE -> AttendanceTypeEntity.Late
                ADMIT -> AttendanceTypeEntity.Admit
                else -> error("알 수 없는 AttendanceType입니다.")
            }
        }
    }

}