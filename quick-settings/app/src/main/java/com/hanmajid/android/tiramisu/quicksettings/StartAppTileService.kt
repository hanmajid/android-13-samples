package com.hanmajid.android.tiramisu.quicksettings

import android.content.Intent
import android.os.Build
import android.service.quicksettings.Tile
import android.service.quicksettings.TileService
import androidx.annotation.RequiresApi

/**
 * Simple [TileService] that starts the application when the Quick Settings tile is clicked.
 */
@RequiresApi(Build.VERSION_CODES.N)
class StartAppTileService : TileService() {
    override fun onStartListening() {
        super.onStartListening()
        qsTile?.apply {
            state = Tile.STATE_ACTIVE
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                subtitle = "Open App"
            }
            updateTile()
        }
    }

    override fun onClick() {
        super.onClick()

        startActivityAndCollapse(
            Intent(this, MainActivity::class.java)
                .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        )
    }
}