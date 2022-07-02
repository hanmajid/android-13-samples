package com.hanmajid.android.tiramisu.nearbywifidevices

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.net.wifi.WifiManager
import android.os.Build
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.google.android.material.dialog.MaterialAlertDialogBuilder

class MainActivity : AppCompatActivity() {
    private lateinit var requestPermissionLauncher: ActivityResultLauncher<String>
    private lateinit var startButton: Button
    private lateinit var statusTextView: TextView
    private val wifiManager: WifiManager by lazy {
        applicationContext.getSystemService(Context.WIFI_SERVICE) as WifiManager
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        requestPermissionLauncher =
            registerForActivityResult(
                ActivityResultContracts.RequestPermission()
            ) { isGranted: Boolean ->
                if (isGranted) {
                    startLocalOnlyHotspot()
                } else {
                    Toast.makeText(
                        this,
                        "Please allow the Nearby Wi-Fi Devices permission for this app",
                        Toast.LENGTH_LONG,
                    ).show()
                }
            }
        startButton = findViewById(R.id.button_start_local_only_hotspot)
        statusTextView = findViewById(R.id.text_local_only_hotspot_status)

        startButton.setOnClickListener {
            checkPermissionOrStartLocalOnlyHotspot()
        }
    }

    @SuppressLint("InlinedApi")
    private fun checkPermissionOrStartLocalOnlyHotspot() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val permission: String = Manifest.permission.NEARBY_WIFI_DEVICES
            when {
                ContextCompat.checkSelfPermission(
                    this,
                    permission,
                ) == PackageManager.PERMISSION_GRANTED -> {
                    startLocalOnlyHotspot()
                }
                shouldShowRequestPermissionRationale(permission) -> {
                    MaterialAlertDialogBuilder(this)
                        .setMessage("This app would not work without Nearby Wi-Fi Devices permission. Do you want to give this app the permission?")
                        .setPositiveButton("Yes") { _, _ ->
                            requestPermissionLauncher.launch(permission)
                        }.setNegativeButton("No Thanks") { _, _ ->

                        }.show()
                }
                else -> {
                    requestPermissionLauncher.launch(permission)
                }
            }
        } else {
            Toast.makeText(
                this,
                "Please use Android 13 device.",
                Toast.LENGTH_SHORT,
            ).show()
        }
    }

    @SuppressLint("NewApi")
    private fun startLocalOnlyHotspot() {
        wifiManager.startLocalOnlyHotspot(
            object : WifiManager.LocalOnlyHotspotCallback() {
                override fun onStarted(reservation: WifiManager.LocalOnlyHotspotReservation?) {
                    super.onStarted(reservation)
                    startButton.isEnabled = false
                    statusTextView.text = "Status Local Only Hotspot: STARTED"
                }

                override fun onFailed(reason: Int) {
                    super.onFailed(reason)
                    Toast.makeText(
                        this@MainActivity,
                        "Error Local Only Hotspot: $reason",
                        Toast.LENGTH_SHORT,
                    ).show()
                }

                override fun onStopped() {
                    super.onStopped()
                    startButton.isEnabled = true
                    statusTextView.text = "Status Local Only Hotspot: STOPPED"
                }
            },
            null,
        )
    }
}