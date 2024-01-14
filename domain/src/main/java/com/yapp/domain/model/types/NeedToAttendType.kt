package com.yapp.domain.model.types

enum class NeedToAttendType(val value: String) {
    NEED_ATTENDANCE("필수 출석체크"),
    DONT_NEED_ATTENDANCE("선택 출석체크"),
    DAY_OFF("휴얍");
}
