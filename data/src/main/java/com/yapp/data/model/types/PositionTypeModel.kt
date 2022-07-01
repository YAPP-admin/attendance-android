package com.yapp.data.model.types

import com.yapp.domain.model.types.PositionTypeEntity


enum class PositionTypeModel {
    DEV_ANDROID, DEV_WEB, DEV_IOS, DEV_SERVER, DESIGNER, PROJECT_MANAGER;

    companion object {
        fun PositionTypeModel.mapToEntity(): PositionTypeEntity {
            return when (this) {
                DEV_ANDROID -> PositionTypeEntity.DEV_ANDROID
                DEV_WEB -> PositionTypeEntity.DEV_WEB
                DEV_IOS -> PositionTypeEntity.DEV_IOS
                DEV_SERVER -> PositionTypeEntity.DEV_SERVER
                DESIGNER -> PositionTypeEntity.DESIGNER
                PROJECT_MANAGER -> PositionTypeEntity.PROJECT_MANAGER
            }
        }
    }

}