package com.yapp.data.model.types

import com.yapp.domain.model.types.PlatformTypeEntity


enum class PlatformTypeModel{
    ANDROID, IOS, WEB_FRONTEND, WEB_BACKEND;

    companion object {
        fun PlatformTypeModel.mapToEntity(): PlatformTypeEntity {
            return when (this) {
                ANDROID -> PlatformTypeEntity.ANDROID
                IOS -> PlatformTypeEntity.IOS
                WEB_FRONTEND -> PlatformTypeEntity.WEB_FRONTEND
                WEB_BACKEND -> PlatformTypeEntity.WEB_BACKEND
            }
        }
    }

}