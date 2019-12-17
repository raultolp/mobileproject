package com.example.travelblog

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.hardware.Camera
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ArrayAdapter
import android.widget.ListView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProviders
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.travelblog.room.BlogEntity
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {

    lateinit var mapsActivity: MapsActivity
    private val TAG = "MYAPI"
    private lateinit var model: BlogViewModel
    lateinit var blogTitlesAdapter: BlogTitlesAdapter

    val permissions = arrayOf(
        android.Manifest.permission.CAMERA,
        android.Manifest.permission.WRITE_EXTERNAL_STORAGE,
        android.Manifest.permission.ACCESS_FINE_LOCATION)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //TODO: toolbar
        //TODO: permissions
        // TODO: NB! LÕPUS KASUTA Camera.release()

        val db = LocalDbClient.getDatabase(this)!!
        //db?.getBlogDao()?.deleteAllBlogs()  //clean up


        //Link to ViewModel:
        model = ViewModelProviders.of(this).get(BlogViewModel::class.java)
        blogTitlesAdapter = BlogTitlesAdapter(model, this)
        titleListView.adapter = blogTitlesAdapter //adapter is set on listView


        //DATABASE KATSETUS:
        //Inserting data:
        var blog1Title = "title 1"
        var blog1Desc = "desc 1"
        var blog1 = BlogEntity(0, blog1Title, blog1Desc) //0 corresponds to 'no value', autogenerates id
        db?.getBlogDao()?.addBlog(blog1)

        var blog2Title = "title 2"
        var blog2Desc = "desc 2"
        var blog2 = BlogEntity(0, blog2Title, blog2Desc) //0 corresponds to 'no value', autogenerates id
        db?.getBlogDao()?.addBlog(blog2)

        //Retrieving data:
        val blogs = db.getBlogDao().loadBlogs()
        var firstBlog = blogs.get(0)
        Log.i("RESULT: ", ""+ firstBlog.blogId + ", " +firstBlog.blogTitle + ", " + firstBlog.blogDescription)
        model.refresh()
        blogTitlesAdapter.notifyDataSetChanged()


/*        //LAUNCHING MAP ACTIVITY:
        mapsActivity = MapsActivity()
        showMap()*/
    }

    //OPENING EXISTING BLOG (FROM LISTVIEW):
    fun openBlog(id: Int) {  //view: View
        val intent = Intent(this, BlogActivity::class.java)
        intent.putExtra("edit mode", false)
        intent.putExtra("blog id", id)
        startActivityForResult(intent, 1)
    }

    //OPENING NEW BLOG ('NEW' BUTTON):
    fun createBlog(view: View) {
        val intent = Intent(this, BlogActivity::class.java)
        intent.putExtra("edit mode", true)
        intent.putExtra("blog id", -1)
        startActivityForResult(intent, 1)  //need to get back the name of new blog
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode==1 && resultCode==2){
            model.refresh()
            blogTitlesAdapter.notifyDataSetChanged()
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

    // TODO : Fix needed
//    override fun onStart() {
//        super.onStart()
//        if (hasNoPermissions()) {
//            requestPermission()
//        }else{
//            checkCameraHardware(this)
//        }
//    }

    private fun hasNoPermissions(): Boolean{
        return ContextCompat.checkSelfPermission(this,
            Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED || ContextCompat.checkSelfPermission(this,
            Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED || ContextCompat.checkSelfPermission(this,
            Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED
    }

    fun requestPermission(){
        ActivityCompat.requestPermissions(this, permissions,0)
    }

    override fun onStop() {
        super.onStop()
// TODO        Camera.release()
    }

    override fun onResume() {
        super.onResume()
        if(!hasNoPermissions()){
            val intent = Intent(baseContext, MainActivity::class.java)
            startActivity(intent)
            finish()
        } else {
            model.refresh()  //tries to refresh the model, if DB has changed. Model will then update the in-memory list.
            blogTitlesAdapter.notifyDataSetChanged() //also tries to update adapter, because adapter is using a list from the model
        }
    }
}
