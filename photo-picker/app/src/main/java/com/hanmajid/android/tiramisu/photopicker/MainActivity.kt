package com.hanmajid.android.tiramisu.photopicker

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.snackbar.Snackbar

class MainActivity : AppCompatActivity() {
    private lateinit var pickSingleMediaLauncher: ActivityResultLauncher<Intent>
    private lateinit var pickMultipleMediaLauncher: ActivityResultLauncher<Intent>

    @SuppressLint("NewApi")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Initialize single media picker launcher
        pickSingleMediaLauncher =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
                if (it.resultCode != Activity.RESULT_OK) {
                    Toast.makeText(this, "Failed picking media.", Toast.LENGTH_SHORT).show()
                } else {
                    val uri = it.data?.data
                    showSnackBar("SUCCESS: ${uri?.path}")
                }
            }
        // Initialize multiple media picker launcher
        pickMultipleMediaLauncher =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
                if (it.resultCode != Activity.RESULT_OK) {
                    Toast.makeText(this, "Failed picking media.", Toast.LENGTH_SHORT).show()
                } else {
                    val uris = it.data?.clipData ?: return@registerForActivityResult
                    var uriPaths = ""
                    for (index in 0 until uris.itemCount) {
                        uriPaths += uris.getItemAt(index).uri.path
                        uriPaths += "\n"
                    }
                    showSnackBar("SUCCESS: $uriPaths")
                }
            }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            // Setup pick 1 image/video
            findViewById<Button>(R.id.button_pick_photo_video).setOnClickListener {
                pickSingleMediaLauncher.launch(
                    Intent(MediaStore.ACTION_PICK_IMAGES)
                )
            }
            // Setup pick 1 image
            findViewById<Button>(R.id.button_pick_photo).setOnClickListener {
                pickSingleMediaLauncher.launch(
                    Intent(MediaStore.ACTION_PICK_IMAGES)
                        .apply {
                            type = "image/*"
                        }
                )
            }
            // Setup pick 1 video
            findViewById<Button>(R.id.button_pick_video).setOnClickListener {
                pickSingleMediaLauncher.launch(
                    Intent(MediaStore.ACTION_PICK_IMAGES)
                        .apply {
                            type = "video/*"
                        }
                )
            }
            // Setup pick 3 images
            findViewById<Button>(R.id.button_pick_3_photos).setOnClickListener {
                pickMultipleMediaLauncher.launch(
                    Intent(MediaStore.ACTION_PICK_IMAGES)
                        .apply {
                            type = "image/*"
                            putExtra(MediaStore.EXTRA_PICK_IMAGES_MAX, 3)
                        }
                )
            }
            // Setup max pick medias
            val maxPickMedia = MediaStore.getPickImagesMaxLimit()
            findViewById<TextView>(R.id.text_mack_pick_media).text = "Max Pick Media: $maxPickMedia"
        } else {
            Toast.makeText(this, "Please use Android 13 device.", Toast.LENGTH_SHORT).show()
        }
    }

    /**
     * Shows [message] in a [Snackbar].
     */
    private fun showSnackBar(message: String) {
        val snackBar = Snackbar.make(
            findViewById(android.R.id.content),
            message,
            Snackbar.LENGTH_LONG,
        )
        // Set the max lines of SnackBar
        snackBar.view.findViewById<TextView>(com.google.android.material.R.id.snackbar_text).maxLines =
            10
        snackBar.show()
    }
}