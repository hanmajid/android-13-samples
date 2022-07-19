package com.hanmajid.android.tiramisu.copyandpaste

import android.content.ClipData
import android.content.ClipDescription
import android.content.ClipboardManager
import android.content.Context
import android.os.Build
import android.os.Bundle
import android.os.PersistableBundle
import android.view.View
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.snackbar.Snackbar

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Setup button click listeners
        findViewById<Button>(R.id.button_copy).setOnClickListener {
            copyPlainTextToClipBoard(
                context = this,
                label = "text_normal",
                text = "This is copied text !",
            )
        }
        findViewById<Button>(R.id.button_copy_credential).setOnClickListener {
            copyPlainTextToClipBoard(
                context = this,
                label = "text_confidential",
                text = "This is copied confidential text !",
                isConfidential = true,
            )
        }
    }

    /**
     * Copy [text] to clipboard. Mark the content as sensitive by setting [isConfidential] to true.
     */
    private fun copyPlainTextToClipBoard(
        context: Context,
        label: String,
        text: String,
        isConfidential: Boolean = false,
    ) {
        val clipboardManager = context.getSystemService(CLIPBOARD_SERVICE) as ClipboardManager
        val clipData = ClipData.newPlainText(
            label,
            text
        ).apply {
            if (isConfidential) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                    description.extras = PersistableBundle().apply {
                        putBoolean(ClipDescription.EXTRA_IS_SENSITIVE, true)
                    }
                } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    description.extras = PersistableBundle().apply {
                        putBoolean("android.content.extra.IS_SENSITIVE", true)
                    }
                }
            }
        }
        clipboardManager.setPrimaryClip(clipData)
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.TIRAMISU) {
            Snackbar.make(
                findViewById<View>(android.R.id.content).rootView,
                "Text copied!",
                Snackbar.LENGTH_SHORT,
            ).show()
        }
    }
}