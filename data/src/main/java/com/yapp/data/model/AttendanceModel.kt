package com.yapp.data.model

import com.yapp.data.Constants.ABSENT
import com.yapp.data.Constants.EMPTY
import com.yapp.data.Constants.LATE
import com.yapp.data.Constants.NORMAL
import com.yapp.data.Constants.REPORTED_ABSENT
import com.yapp.data.Constants.REPORTED_LATE
import com.yapp.domain.model.AttendanceTypeEntity


data class AttendanceModel(val point: Int = 0, val text: String = "EMPTY") {

    companion object {
        fun AttendanceModel.mapToEntity(): AttendanceTypeEntity {
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