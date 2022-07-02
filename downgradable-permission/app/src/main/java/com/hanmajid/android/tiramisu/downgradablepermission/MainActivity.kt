package com.hanmajid.android.tiramisu.downgradablepermission

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts.RequestMultiplePermissions
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.google.android.material.snackbar.Snackbar
import kotlin.system.exitProcess

class MainActivity : AppCompatActivity() {
    private var snackBar: Snackbar? = null
    private lateinit var requestPermissionsLauncher: ActivityResultLauncher<Array<String>>
    private lateinit var buttonRequestPermissions: Button
    private lateinit var buttonRevokeCameraPermission: Button
    private lateinit var buttonRevokeCallPermission: Button
    private lateinit var buttonRevokeAllPermissions: Button
    private lateinit var textCameraPermission: TextView
    private lateinit var textCallPermission: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Sets up permissions request launcher.
        requestPermissionsLauncher = registerForActivityResult(RequestMultiplePermissions()) {
            refreshUI()
        }

        // Initialize UI.
        initializeUI()

        // Refresh UI.
        refreshUI()
    }

    /**
     * Initialize UI elements for the first time.
     */
    private fun initializeUI() {
        buttonRequestPermissions = findViewById(R.id.button_request_permission)
        buttonRevokeCameraPermission = findViewById(R.id.button_revoke_camera_permission)
        buttonRevokeCallPermission = findViewById(R.id.button_revoke_call_permission)
        buttonRevokeAllPermissions = findViewById(R.id.button_revoke_all_permissions)
        textCameraPermission = findViewById(R.id.text_camera_permission)
        textCallPermission = findViewById(R.id.text_call_permission)

        buttonRequestPermissions.setOnClickListener {
            if (requiredPermissions.all { isPermissionGranted(it) }) {
                refreshUI()
            } else {
                requestPermissionsLauncher.launch(requiredPermissions)
            }
        }
        buttonRevokeCameraPermission.setOnClickListener {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                revokeSelfPermissionOnKill(Manifest.permission.CAMERA)
                showRevokeSuccessSnackBar()
            }
        }
        buttonRevokeCallPermission.setOnClickListener {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                revokeSelfPermissionOnKill(Manifest.permission.CALL_PHONE)
                showRevokeSuccessSnackBar()
            }
        }
        buttonRevokeAllPermissions.setOnClickListener {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                revokeSelfPermissionsOnKill(requiredPermissions.toList())
                showRevokeSuccessSnackBar()
            }
        }
    }

    /**
     * Refresh UI elements.
     */
    private fun refreshUI() {
        val isCameraPermissionGranted = isPermissionGranted(Manifest.permission.CAMERA)
        val isCallPermissionGranted = isPermissionGranted(Manifest.permission.CALL_PHONE)

        // Updates permission texts
        textCameraPermission.text = if (isCameraPermissionGranted) "GRANTED" else "NOT GRANTED"
        textCallPermission.text = if (isCallPermissionGranted) "GRANTED" else "NOT GRANTED"

        // Updates buttons isEnabled
        buttonRevokeCameraPermission.isEnabled = isCameraPermissionGranted
        buttonRevokeCallPermission.isEnabled = isCallPermissionGranted
        buttonRevokeAllPermissions.isEnabled = isCallPermissionGranted && isCameraPermissionGranted
    }

    /**
     * Returns true if [permission] is granted.
     */
    private fun isPermissionGranted(permission: String): Boolean {
        return ContextCompat.checkSelfPermission(
            this,
            permission,
        ) == PackageManager.PERMISSION_GRANTED
    }

    /**
     * Shows [Snackbar] telling user to restart the app.
     */
    private fun showRevokeSuccessSnackBar() {
        snackBar?.dismiss()
        snackBar = Snackbar.make(
            findViewById<View>(android.R.id.content).rootView,
            "Permission has been revoked! Restart app to see the change.",
            Snackbar.LENGTH_INDEFINITE,
        ).apply {
            setAction("Quit App") {
                exitProcess(0)
            }
        }
        snackBar?.show()
    }

    companion object {
        /**
         * App's required permissions.
         */
        val requiredPermissions = arrayOf(
            Manifest.permission.CAMERA,
            Manifest.permission.CALL_PHONE,
        )
    }
}