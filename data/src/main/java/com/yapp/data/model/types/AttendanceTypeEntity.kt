package com.yapp.data.model.types

import com.yapp.data.model.types.AttendanceTypeEntity.Companion.POINT_ABSENT
import com.yapp.data.model.types.AttendanceTypeEntity.Companion.POINT_ADMIT
import com.yapp.data.model.types.AttendanceTypeEntity.Companion.POINT_LATE
import com.yapp.data.model.types.AttendanceTypeEntity.Companion.POINT_NORMAL
import com.yapp.data.model.types.AttendanceTypeEntity.Companion.TEXT_ABSENT
import com.yapp.data.model.types.AttendanceTypeEntity.Companion.TEXT_ADMIT
import com.yapp.data.model.types.AttendanceTypeEntity.Companion.TEXT_LATE
import com.yapp.data.model.types.AttendanceTypeEntity.Companion.TEXT_NORMAL
import com.yapp.domain.model.types.AttendanceType
import kotlinx.serialization.Serializable


@Serializable
data class AttendanceTypeEntity(val point: Int = POINT_ABSENT, val text: String = TEXT_ABSENT) {

    companion object {
        const val TEXT_ABSENT = "결석"
        const val POINT_ABSENT =  -20

        const val TEXT_NORMAL = "출석"
        const val POINT_NORMAL = 0

        const val TEXT_LATE = "지각"
        const val POINT_LATE = -10

        const val TEXT_ADMIT = "출석 인정"
        const val POINT_ADMIT = 0
    }

}

fun AttendanceTypeEntity.toDomain(): AttendanceType {
    return when (this.text) {
        AttendanceTypeEntity.TEXT_ABSENT -> AttendanceType.Absent
        AttendanceTypeEntity.TEXT_NORMAL -> AttendanceType.Normal
        AttendanceTypeEntity.TEXT_LATE -> AttendanceType.Late
        AttendanceTypeEntity.TEXT_ADMIT -> AttendanceType.Admit
        else -> error("알 수 없는 AttendanceType입니다.")
    }
}

fun AttendanceType.toData(): AttendanceTypeEntity {
    return when (this) {
        AttendanceType.Absent -> AttendanceTypeEntity(point = POINT_ABSENT, text = TEXT_ABSENT)
        AttendanceType.Normal -> AttendanceTypeEntity(point = POINT_NORMAL, text = TEXT_NORMAL)
        AttendanceType.Late -> AttendanceTypeEntity(point = POINT_LATE, text = TEXT_LATE)
        AttendanceType.Admit -> AttendanceTypeEntity(point = POINT_ADMIT, text = TEXT_ADMIT)
        else -> error("알 수 없는 AttendanceType입니다.")
    }
}