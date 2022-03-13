package com.yapp.data.model.types

import com.yapp.domain.model.types.PositionTypeEntity


enum class PositionTypeModel {
    FRONTEND, BACKEND, DESIGNER, PROJECT_MANAGER;

    companion object {
        fun PositionTypeModel.mapToEntity(): PositionTypeEntity {
            return when (this) {
                FRONTEND -> PositionTypeEntity.FRONTEND
                BACKEND -> PositionTypeEntity.BACKEND
                DESIGNER -> PositionTypeEntity.DESIGNER
                PROJECT_MANAGER -> PositionTypeEntity.PROJECT_MANAGER
            }
        }
    }

}