package com.hanmajid.android.tiramisu.quicksettings

import android.annotation.SuppressLint
import android.app.StatusBarManager
import android.content.ComponentName
import android.content.Context
import android.graphics.drawable.Icon
import android.os.Build
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    @SuppressLint("NewApi", "WrongConstant")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        findViewById<Button>(R.id.button_add_quick_settings).setOnClickListener {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                val statusBarManager =
                    getSystemService(Context.STATUS_BAR_SERVICE) as StatusBarManager
                /**
                 * Important: Change the package name accordingly to your own package name!
                 */
                statusBarManager.requestAddTileService(
                    ComponentName(
                        "com.hanmajid.android.tiramisu.quicksettings",
                        "com.hanmajid.android.tiramisu.quicksettings.StartAppTileService",
                    ),
                    "My Amazing App",
                    Icon.createWithResource(this, R.drawable.ic_launcher_foreground),
                    {},
                    {}
                )
            } else {
                Toast.makeText(
                    this,
                    "`requestAddTileService` can only be called in Android 13/Tiramisu.",
                    Toast.LENGTH_SHORT,
                ).show()
            }
        }
    }
}