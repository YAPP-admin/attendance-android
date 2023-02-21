package com.yapp.data.model.types

import com.yapp.domain.model.types.PositionType


enum class PositionTypeEntity {
    DEV_ANDROID, DEV_WEB, DEV_IOS, DEV_SERVER, DESIGNER, PROJECT_MANAGER;
}

fun PositionTypeEntity.toDomain(): PositionType {
    return when (this) {
        PositionTypeEntity.DEV_ANDROID -> PositionType.DEV_ANDROID
        PositionTypeEntity.DEV_WEB -> PositionType.DEV_WEB
        PositionTypeEntity.DEV_IOS -> PositionType.DEV_IOS
        PositionTypeEntity.DEV_SERVER -> PositionType.DEV_SERVER
        PositionTypeEntity.DESIGNER -> PositionType.DESIGNER
        PositionTypeEntity.PROJECT_MANAGER -> PositionType.PROJECT_MANAGER
    }
}

fun PositionType.toData(): PositionTypeEntity {
    return when (this) {
        PositionType.DEV_ANDROID -> PositionTypeEntity.DEV_ANDROID
        PositionType.DEV_WEB -> PositionTypeEntity.DEV_WEB
        PositionType.DEV_IOS -> PositionTypeEntity.DEV_IOS
        PositionType.DEV_SERVER -> PositionTypeEntity.DEV_SERVER
        PositionType.DESIGNER -> PositionTypeEntity.DESIGNER
        PositionType.PROJECT_MANAGER -> PositionTypeEntity.PROJECT_MANAGER
    }
}