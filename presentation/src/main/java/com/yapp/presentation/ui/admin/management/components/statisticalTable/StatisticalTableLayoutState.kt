package com.yapp.presentation.ui.admin.management.components.statisticalTable


data class StatisticalTableLayoutState(
    val totalCount: Int = 0,
    val attendCount: Int = 0,
    val tardyCount: Int = 0,
    val absentCount: Int = 0,
    val admitCount: Int = 0,
) {

    /**
     * 출석 인정, 지각, 출석 3개는 현재 **출석**한 인원이기 때문에
     * 현재 출석한 인원에 포함한다.
     */
    val currentAttendCount: Int
        get() = attendCount + tardyCount + admitCount

}