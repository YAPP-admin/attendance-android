package com.yapp.data.model

import com.yapp.data.model.AttendanceTypeModel.Companion.toEntity
import com.yapp.domain.model.MemberEntity


internal class MemberModel(
    val id: Long?,
    val name: String?,
    val position: String?,
    val team: String?,
    val isAdmin: Boolean? = false,
    val attendances: List<AttendanceTypeModel>?
) {

    companion object {
        fun MemberModel.toEntity(): MemberEntity {
            return MemberEntity(
                id = id,
                name = name,
                position = position,
                team = team,
                isAdmin = isAdmin,
                attendances = attendances?.map { it.toEntity() }
            )
        }

        const val MAX_SCORE = 100
    }

    fun getTotalAttendanceScore(): Int {
        return MAX_SCORE - attendances?.map { it.point }!!.sum()
    }

    fun getTodayAttendance(session_id: Int): AttendanceTypeModel {
        return attendances?.get(session_id) ?: AttendanceTypeModel.Empty
    }

}