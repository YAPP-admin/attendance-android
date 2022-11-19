package com.yapp.presentation.model


//data class Attendance(
//    val sessionId: Int,
//    val attendanceType: AttendanceType
//) {
//
//    companion object {
//        fun Attendance.mapTo(): Attendance {
//            return Attendance(
//                sessionId = this.sessionId,
//                attendanceType = this.type.mapTo()
//            )
//        }
//
//        fun Attendance.mapToEntity(): Attendance {
//            return Attendance(
//                sessionId = sessionId,
//                type = this.attendanceType.mapToEntity()
//            )
//        }
//    }
//
//}
//
//sealed class AttendanceType(val point: Int, val text: String) {
//
//    companion object {
//        fun AttendanceType.mapTo(): AttendanceType {
//            return when (this) {
//                is com.yapp.domain.model.AttendanceTypeEntity.AttendanceType.Absent -> AttendanceType.Absent
//                is com.yapp.domain.model.AttendanceTypeEntity.AttendanceType.Normal -> AttendanceType.Normal
//                is com.yapp.domain.model.AttendanceTypeEntity.AttendanceType.Late -> AttendanceType.Late
//                is com.yapp.domain.model.AttendanceTypeEntity.AttendanceType.Admit -> AttendanceType.Admit
//            }
//        }
//
//        fun AttendanceType.mapToEntity(): AttendanceType {
//            return when (this) {
//                is AttendanceType.Absent -> AttendanceType.Absent
//                is AttendanceType.Normal -> AttendanceType.Normal
//                is AttendanceType.Late -> AttendanceType.Late
//                is AttendanceType.Admit -> AttendanceType.Admit
//            }
//        }
//
//        fun getAllTypes() : List<AttendanceType> {
//            return listOf(Absent, Normal, Late, Admit)
//        }
//    }
//
//    object Absent : AttendanceType(point = -20, text = "결석")
//    object Normal : AttendanceType(point = 0, text = "출석")
//    object Late : AttendanceType(point = -10, text = "지각")
//    object Admit : AttendanceType(point = 0, text = "출석 인정")
//}