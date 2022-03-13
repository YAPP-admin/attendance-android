package com.yapp.presentation.util.permission

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity

object PermissionManager {
    fun requestPermission(
        context: AppCompatActivity,
        vararg permissionType: PermissionType,
        permissionCallback: (state: PermissionState) -> Unit
    ) {
        launchPermissionActivity(context, permissionType, permissionCallback)
    }

    private fun launchPermissionActivity(
        context: AppCompatActivity,
        type: Array<out PermissionType>,
        permissionCallback: (state: PermissionState) -> Unit
    ) {
        PermissionActivity.callback = permissionCallback

        val intent = Intent(context, PermissionActivity::class.java)
        intent.putExtra(
            "bundle", PermissionBundle(
                permissions = getPermissionsFlag(type)
            )
        )
        context.startActivity(intent)
    }
}