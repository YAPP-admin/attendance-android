package com.yapp.data.model.types

import com.yapp.domain.model.types.PositionType


enum class PositionTypeEntity {
    PROJECT_MANAGER,
    DESIGNER,
    DEV_ANDROID,
    DEV_IOS,
    DEV_WEB,
    DEV_SERVER,
    DEV_FLUTTER;
}

fun PositionTypeEntity.toDomain(): PositionType {
    return when (this) {
        PositionTypeEntity.PROJECT_MANAGER -> PositionType.PROJECT_MANAGER
        PositionTypeEntity.DESIGNER -> PositionType.DESIGNER
        PositionTypeEntity.DEV_ANDROID -> PositionType.DEV_ANDROID
        PositionTypeEntity.DEV_IOS -> PositionType.DEV_IOS
        PositionTypeEntity.DEV_WEB -> PositionType.DEV_WEB
        PositionTypeEntity.DEV_SERVER -> PositionType.DEV_SERVER
        PositionTypeEntity.DEV_FLUTTER -> PositionType.DEV_FLUTTER
    }
}

fun PositionType.toData(): PositionTypeEntity {
    return when (this) {
        PositionType.PROJECT_MANAGER -> PositionTypeEntity.PROJECT_MANAGER
        PositionType.DESIGNER -> PositionTypeEntity.DESIGNER
        PositionType.DEV_ANDROID -> PositionTypeEntity.DEV_ANDROID
        PositionType.DEV_IOS -> PositionTypeEntity.DEV_IOS
        PositionType.DEV_WEB -> PositionTypeEntity.DEV_WEB
        PositionType.DEV_SERVER -> PositionTypeEntity.DEV_SERVER
        PositionType.DEV_FLUTTER -> PositionTypeEntity.DEV_FLUTTER
    }
}
