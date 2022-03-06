package com.yapp.data.model

import com.yapp.data.model.AttendanceModel.Companion.mapToEntity
import com.yapp.data.model.AttendanceTypeModel.Companion.mapToEntity
import com.yapp.domain.model.MemberEntity


class MemberModel(
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
                id = id ?: -1,
                name = name ?: "",
                position = position ?: "",
                team = team ?: "",
                isAdmin = isAdmin ?: false,
                attendances = attendances?.map { it.mapToEntity() } ?: emptyList()
            )
        }
    }

}