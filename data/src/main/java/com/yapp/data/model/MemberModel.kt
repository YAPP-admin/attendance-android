package com.yapp.data.model

import com.yapp.data.model.AttendanceModel.Companion.mapToEntity
import com.yapp.data.model.TeamModel.Companion.mapToEntity
import com.yapp.data.model.types.PositionTypeModel
import com.yapp.data.model.types.PositionTypeModel.Companion.mapToEntity
import com.yapp.domain.model.MemberEntity


data class MemberModel(
    val id: Long? = null,
    val name: String? = null,
    val position: PositionTypeModel? = null,
    val team: TeamModel? = null,
    val attendances: List<AttendanceModel>? = null
) {

    companion object {
        fun MemberModel.mapToEntity(): MemberEntity {
            return MemberEntity(
                id = id!!,
                name = name!!,
                position = position!!.mapToEntity(),
                team = team!!.mapToEntity(),
                attendances = attendances?.map { it.mapToEntity() }!!
            )
        }
    }

}