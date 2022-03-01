package com.yapp.data.model

import com.yapp.data.model.AttendanceModel.Companion.mapTo
import com.yapp.domain.model.MemberEntity


internal class MemberModel(
    val id: Long? = 0L,
    val name: String? = "",
    val position: String? = "",
    val team: String? = "",
    val isAdmin: Boolean? = false,
    val attendances: List<AttendanceModel>? = emptyList()
) {

    companion object {
        fun MemberModel.mapTo(): MemberEntity {
            return MemberEntity(
                id = id,
                name = name,
                position = position,
                team = team,
                isAdmin = isAdmin,
                attendances = attendances?.map { it.mapTo() }
            )
        }

        const val MAX_SCORE = 100
    }

    fun getTotalAttendanceScore(): Int {
        return MAX_SCORE - attendances?.map { it.point }!!.sum()
    }

    fun getTodayAttendance(session_id: Int): AttendanceModel {
        return attendances?.get(session_id) ?: AttendanceModel()
    }

}