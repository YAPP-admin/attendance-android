package com.yapp.presentation.util.permission

import android.content.pm.PackageManager
import android.os.Bundle
import android.os.Parcelable
import android.view.WindowManager
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import kotlinx.parcelize.Parcelize

enum class PermissionState {
    GRANTED,
    DENIED,
    NEED_DESCRIPTION
}

@Parcelize
data class PermissionBundle(
    val permissions: List<String>
) : Parcelable

class PermissionActivity : AppCompatActivity() {
    companion object {
        var callback: ((isGranted: PermissionState) -> Unit)? = null
    }

    private val bundle: PermissionBundle? by lazy {
        intent.getParcelableExtra("bundle")
    }

    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        bundle?.let { it ->
            if (permissions.entries.filter { it.value }.size == it.permissions.size) {
                permissionCallback(PermissionState.GRANTED)
            } else {
                permissionCallback(PermissionState.DENIED)
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        overridePendingTransition(0, 0)
        super.onCreate(savedInstanceState)
        window.addFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)
        bundle?.let {
            launchPermission(it.permissions)
        }
    }

    private fun launchPermission(permissions: List<String>) {
        if (permissions.isEmpty())
            finish()

        val permissionState = isAllPermissionGranted(permissions)
        val rationalState = isRationaleNeed(permissions)

        when {
            permissionState -> permissionCallback(PermissionState.GRANTED)
            rationalState -> permissionCallback(PermissionState.NEED_DESCRIPTION)
            else -> requestPermissionLauncher.launch(permissions.toTypedArray())
        }
    }

    private fun isAllPermissionGranted(
        requestedPermissions: List<String>
    ): Boolean {
        requestedPermissions.forEach {
            val grantedState = ContextCompat.checkSelfPermission(this, it)
            if (grantedState != PackageManager.PERMISSION_GRANTED) {
                return false
            }
        }
        return true
    }

    private fun isRationaleNeed(
        requestedPermissions: List<String>
    ): Boolean {
        requestedPermissions.forEach {
            if (shouldShowRequestPermissionRationale(it)) {
                return true
            }
        }
        return false
    }

    private fun permissionCallback(state: PermissionState) {
        callback?.let {
            it(state)
            finish()
        }
    }
}