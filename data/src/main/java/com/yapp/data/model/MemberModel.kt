package com.yapp.data.model

import com.yapp.data.model.AttendanceModel.Companion.mapToEntity
import com.yapp.data.model.AttendanceTypeModel.Companion.mapToEntity
import com.yapp.data.model.MemberModel.Companion.mapTo
import com.yapp.data.model.TeamModel.Companion.mapToEntity
import com.yapp.data.model.types.PositionTypeModel
import com.yapp.data.model.types.PositionTypeModel.Companion.mapToEntity
import com.yapp.domain.model.MemberEntity
import com.yapp.domain.model.TeamEntity


class MemberModel(
    val id: Long? = null,
    val name: String? = null,
    val position: PositionTypeModel? = null,
    val team: TeamModel? = null,
    val isAdmin: Boolean? = null,
    val attendances: List<AttendanceModel>? = null
) {

    companion object {
        fun MemberModel.mapTo(): MemberEntity {
            return MemberEntity(
                id = id!!,
                name = name!!,
                position = position!!.mapToEntity(),
                team = team!!.mapToEntity(),
                isAdmin = isAdmin!!,
                attendances = attendances?.map { it.mapToEntity() }!!
            )
        }
    }

}