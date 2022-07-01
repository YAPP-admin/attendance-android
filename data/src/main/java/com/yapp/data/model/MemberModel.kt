package com.yapp.data.model

import com.google.firebase.firestore.PropertyName
import com.yapp.data.model.AttendanceModel.Companion.mapToEntity
import com.yapp.data.model.TeamModel.Companion.mapToEntity
import com.yapp.data.model.types.PositionTypeModel
import com.yapp.data.model.types.PositionTypeModel.Companion.mapToEntity
import com.yapp.domain.model.MemberEntity

data class MemberModel(
    @PropertyName("id")
    val id: Long? = null,
    @PropertyName("name")
    val name: String? = null,
    @PropertyName("position")
    val position: PositionTypeModel? = null,
    @PropertyName("team")
    val team: TeamModel? = null,
    @PropertyName("attendances")
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