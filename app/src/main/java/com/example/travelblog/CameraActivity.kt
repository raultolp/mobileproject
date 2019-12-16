package com.example.travelblog

import android.app.Activity
import android.hardware.Camera
import android.os.Bundle
import android.widget.Button
import android.widget.FrameLayout

class CameraActivity : Activity() {

    private var mCamera: Camera? = null
    private var mPreview: CameraPreview? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_camera)

        // Create an instance of Camera
        mCamera = getCameraInstance()

        mPreview = mCamera?.let {
            // Create our Preview view
            CameraPreview(this, it)
        }

        // Set the Preview view as the content of our activity.
        mPreview?.also {
            val preview: FrameLayout = findViewById(R.id.camera_preview)
            preview.addView(it)
        }

        val captureButton: Button = findViewById(R.id.button_capture)
        captureButton.setOnClickListener {
            // get an image from the camera
// TODO           mCamera?.takePicture(null, null, picture)
        }

    }

    /** A safe way to get an instance of the Camera object. */
    fun getCameraInstance(): android.hardware.Camera? {
        return try {
            android.hardware.Camera.open() // attempt to get a Camera instance
        } catch (e: Exception) {
            // Camera is not available (in use or does not exist)
            null // returns null if camera is unavailable
        }
    }


}