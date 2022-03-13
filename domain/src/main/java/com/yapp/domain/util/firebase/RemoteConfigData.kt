package com.yapp.domain.util.firebase

sealed class RemoteConfigData<T> {
    abstract val key: String
    abstract val defaultValue: T

    object MaginotlineTime : RemoteConfigData<String>() {
        override val key: String = ATTENDANCE_MAGINOTLINE_TIME
        override val defaultValue: String = "fail"
    }

    object SessionList : RemoteConfigData<String>() {
        override val key: String = ATTENDANCE_SESSION_LIST
        override val defaultValue: String = ""
    }

    object Config : RemoteConfigData<String>() {
        override val key: String = ATTENDANCE_CONFIG
        override val defaultValue: String = ""
    }

    object AttendanceSelectTeams : RemoteConfigData<String>() {
        override val key: String = ATTENDANCE_SELECT_TEAMS
        override val defaultValue: String = ""
    }



    companion object {
        private const val ATTENDANCE_MAGINOTLINE_TIME = "attendance_maginotline_time"
        private const val ATTENDANCE_SESSION_LIST = "attendance_session_list"
        private const val ATTENDANCE_SELECT_TEAMS = "attendance_select_teams"
        private const val ATTENDANCE_CONFIG = "config"

        val defaultMaps = mapOf(
            MaginotlineTime.defaultValue to MaginotlineTime.key,
            SessionList.defaultValue to SessionList.key,
            AttendanceSelectTeams.defaultValue to AttendanceSelectTeams.key
            Config.defaultValue to Config.key
        )
    }

}
