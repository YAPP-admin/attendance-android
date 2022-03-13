package com.yapp.presentation.util.permission

import android.Manifest

enum class PermissionType {
    CAMERA
}

fun getPermissionsFlag(type: Array<out PermissionType>): List<String> {
    return type.map {
        when (it) {
            PermissionType.CAMERA -> Manifest.permission.CAMERA
        }
    }
}