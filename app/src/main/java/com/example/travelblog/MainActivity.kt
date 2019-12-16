package com.example.travelblog

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.hardware.Camera
import android.location.Location
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.Marker
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {

    lateinit var mapsActivity: MapsActivity
    private val TAG = "MYAPI"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //TODO: toolbar
        //TODO: permissions
        //TODO: spinner

        // TODO: NB! LÕPUS KASUTA Camera.release()
        checkCameraHardware(this)

/*        //LAUNCHING MAP ACTIVITY:
        mapsActivity = MapsActivity()
        showMap()*/
    }

    //OPENING EXISTING BLOG (FROM SPINNER):
    fun openBlog(view: View) {
        //TODO: pass on database handle through intent (?)
        val intent = Intent(this, BlogActivity::class.java)
        intent.putExtra("edit mode", false)
        intent.putExtra("blog name", "Amazing spots in Tartu") //TODO: get blogName from spinner (vb-olla peaks kaasa panema hoopis blogi ID andmebaasis?)
        startActivityForResult(intent, 1)
    }

    //OPENING NEW BLOG ('NEW' BUTTON):
    fun createBlog(view: View) {
        //TODO: pass on database handle through intent (?)
        val intent = Intent(this, BlogActivity::class.java)
        intent.putExtra("edit mode", true)
        intent.putExtra("blog name", "new blog")
        startActivityForResult(intent, 1)  //need to get back the name of new blog
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        //Getting values from intent:
        if (requestCode==1 && resultCode==2){
            var newBlogName = data?.getStringExtra("blogName")
            //TODO: add name of new blog to spinner, if name not emty string
        }
    }

    //SHOW MAP:
    private fun showMap() {
        val intent = Intent(this, MapsActivity::class.java)
        startActivity(intent)
    }

    /** Check if this device has a camera */
    private fun checkCameraHardware(context: Context): Boolean {
        return if (context.packageManager.hasSystemFeature(PackageManager.FEATURE_CAMERA)) {
            val cameras = Camera.getNumberOfCameras()
//            val info = Camera.getCameraInfo(0)
            Log.i(TAG, "Cameras found: $cameras")
            true
        } else {
            Log.i(TAG, "Camera NOT FOUND!!!")
            false
        }
    }
}
