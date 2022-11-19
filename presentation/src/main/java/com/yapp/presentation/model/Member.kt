package com.yapp.presentation.model


//data class Member(
//    val id: Long,
//    val name: String,
//    val position: PositionType,
//    val team: Team,
//    val attendances: AttendanceList
//) {
//    companion object {
//        fun Member.mapTo(): Member {
//            return Member(
//                id = id,
//                name = name,
//                position = PositionType.valueOf(position.name),
//                team = team.mapTo(),
//                attendances = AttendanceList.of(attendances.map { it.mapTo() })
//            )
//        }
//    }
//}