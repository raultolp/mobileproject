package com.example.travelblog

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
import com.example.travelblog.room.BlogEntity
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {

    lateinit var mapsActivity: MapsActivity
    private val TAG = "MYAPI"
    //lateinit var listAdapter: ArrayAdapter<String>
    lateinit var custAdapter: ownAdapter
    //lateinit var listItems : ListView
    //lateinit var blogTitles: Array<String>  //= arrayOf()
    lateinit var blogIds: Array<Int>
    var blogTitles = arrayOf(String)
    //var blogIds = arrayOf(Int)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //TODO: toolbar
        //TODO: permissions

        // TODO: NB! LÕPUS KASUTA Camera.release()
        checkCameraHardware(this)


        //listItems  = findViewById(R.id.titleListView)
        val db = LocalDbClient.getDatabase(this)!!
        //db?.getBlogDao()?.deleteAllBlogs()  //clean up

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


/*        //LISTVIEW FOR BLOG TITLES:
        var blogTitles = db.getBlogDao().loadBlogTitles()
        var blogIds = db.getBlogDao().loadBlogIds()
        if (blogTitles.size>0){

            //listAdapter = ArrayAdapter(this, R.layout.list_row, blogTitles)
            custAdapter = ownAdapter(blogTitles.toList())
            //listItems.adapter=custAdapter
            titleListView.adapter=custAdapter
        }*/



/*        //LAUNCHING MAP ACTIVITY:
        mapsActivity = MapsActivity()
        showMap()*/
    }

    //OPENING EXISTING BLOG (FROM LISTVIEW):
    fun openBlog(view: View) {
        //TODO: pass on database handle through intent (?)
        val intent = Intent(this, BlogActivity::class.java)
        intent.putExtra("edit mode", false)
        //Log.i("RESULT", listItems.selectedItemPosition.toString())
        //var blogId = blogIds[listItems.selectedItemPosition]

        var itemPosition = Integer.parseInt(view.tag.toString())
        Log.i("RESULT", itemPosition.toString())
        var blogId = blogIds[itemPosition]

        intent.putExtra("blog id", blogId)
        //intent.putExtra("blog name", "Amazing spots in Tartu") //TODO: get blogName from listView (vb-olla peaks kaasa panema hoopis blogi ID andmebaasis?)
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
            //TODO: add name of new blog to listview, if name not emty string
        }
        //listAdapter.notifyDataSetChanged()
        //TÄIDA UUESTI LISTVIEW!
        //custAdapter.notifyDataSetChanged()
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
