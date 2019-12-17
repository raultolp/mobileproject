package com.example.travelblog

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentActivity
import com.example.travelblog.room.BlogDatabase
import com.example.travelblog.room.BlogEntity
import com.example.travelblog.room.BlogItemEntity
import kotlinx.android.synthetic.main.blog_activity.*


class BlogActivity: AppCompatActivity(), AlertDialogFragment.AlertDialogListener {

    var editMode = true
    var selectedPlace = "" //name of selected place (blog item title)
    //var blogName = ""
    var selectedPlaceIsNew = false
    //var description = ""
    //var blogId = -1
    //var blog = BlogEntity(0, "", "")
    var savedBlog = BlogEntity(0, "", "")
    var unsavedBlog = BlogEntity(0, "", "")
    var savedItem = BlogItemEntity(0, 0, "", 0.0, 0.0)
    var unsavedItem = BlogItemEntity(0, 0, "", 0.0, 0.0)
    var selectedItem = BlogItemEntity(0, 0, "", 0.0, 0.0)
    var selectedItemSaved = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.blog_activity)

        var db = LocalDbClient.getDatabase(applicationContext)

        //Getting values passed on through indents:
        editMode = intent.getBooleanExtra("edit mode", false)
        if (!editMode){
            deactivateEditMode()
            var id = intent.getIntExtra("blog id", 0)
            //blog= db?.getBlogDao()?.loadSingleBlog(blogId)!!
            savedBlog= db?.getBlogDao()?.loadSingleBlog(id)!!
            //blogName = blog.blogTitle.toString()
            //description = blog.blogDescription.toString()
            var name =savedBlog.blogTitle.toString()
            var desc = savedBlog.blogDescription.toString()
            blogTitle.text = Editable.Factory.getInstance().newEditable(name)
            blogDescription.text = Editable.Factory.getInstance().newEditable(desc)
            savedItem.blogIdRef=savedBlog.blogId
            unsavedItem.blogIdRef=savedBlog.blogId
        } else {
            activateEditMode()
        }
    }

    //OPEN BUTTON:
    fun openBlogItem(view: View) {
        //TODO: pass on database handle through intent (?)
        val intent = Intent(this, BlogItemActivity::class.java)
        intent.putExtra("editMode", editMode)
        intent.putExtra("isNewItem", selectedPlaceIsNew)
        var blogid : Int
        var itemid : Int
        if (editMode){
            blogid = unsavedBlog.blogId
            //itemid =
        } else {
            blogid = savedBlog.blogId
            //itemid =
        }
        intent.putExtra("blog id", selectedPlace)   //TODO
        //intent.putExtra("placeName", blogName) //TODO: vb-olla peaks kaasa panema hoopis blogi id ja koha ID andmebaasis?
        //TODO: if blog title has not been not entered (for new blog), dont allow opening blog item (show notification)
        startActivity(intent)
    }

    //AJUTINE NUPP:
    fun onPlaceSelected(view: View) {
        //TODO: nupu vajutamise asemel rakenda seda funktsiooni, kui mapsile on uus marker pandud või see ära võetud
        //Seni on ajutine nupp vajalik, sest muidu ei saa edasi Blog Item Activity'sse liikuda
        //TODO: dont allow selecting more than one new location

        selectedPlace ="New place"  // "Tartu"  //TODO: change place name if existing marker was selected
        selectedPlaceIsNew = true  //TODO: change to false if not new place

        var lat = 58.385156  //TODO: get coordinates of selected marker from map
        var lon = 26.725108

        selectedItem = BlogItemEntity(0, 0, selectedPlace, lat, lon)

        if (openItemButton.visibility == View.VISIBLE){
            openItemButton.visibility = View.GONE
            placeName.text = "Select a place"
        } else {
            openItemButton.visibility = View.VISIBLE
            placeName.text = selectedPlace
        }
    }

    //DELETE BUTTON:
    fun deleteBlog(view: View) {
        unsavedBlog = BlogEntity(0, "", "") //changes were not saved

        //TODO: delete blog and related entitites (blogentities, blogitementities)

        var intent = Intent()
        setResult(2, intent) // setResult(resultCode, resultIntent)
        finish() //back to Main Activity
    }

    //SAVE BUTTON:
    fun saveBlog(view: View) {
        var db = LocalDbClient.getDatabase(applicationContext)
        var title = blogTitle.text.toString()
        var desc = blogDescription.text.toString()
        //dont allow saving with blog name being empty string:
/*        if (title.equals("")){
            showAlertDialog("Blog with no title cannot be saved.")
        } else {
            //dont allow saving two blogs with the same name (for new blog):
            if (savedBlog.blogId==0){
                var allTitles = db?.getBlogDao()?.loadBlogTitles()
                if (allTitles!=null && allTitles.size>0 && allTitles.contains(title)){
                    showAlertDialog("Blog with this name already exists.")
                } else {
                    //saving new blog:
                    //if blog doesnt have item:
                    if(selectedItem.latitude==0.0){

                        var id = db?.getBlogDao()?.addBlog(unsavedBlog)
                        savedBlog=unsavedBlog
                    }

                    //insert items:

                }
            } else {
             //saving existing blog:
            //update blog:

            //insert/update items:

            }


        }*/
        //TODO: save (title, desc.) - insert for new, or update for existing

        deactivateEditMode()
        //showAlertDialog("Alert message text")  //tmp
    }

    //CANCEL BUTTON:
    fun cancelInputs(view: View) {
        //TODO:  add content
        // (initial data (data when edit mode is activated) should be saved in variables, and restored if Cancel is pressed)
        deactivateEditMode()
    }

    //EDIT BUTTON:
    fun editInputs(view: View) {
        activateEditMode()
    }

    fun deactivateEditMode() {
        editMode = false
        blogTitle.inputType = 0  // // inputType= "none" (not editable) : https://developer.android.com/reference/android/widget/TextView.html#attr_android:inputType
        blogDescription.inputType = 0
        editButton.visibility = View.VISIBLE
        cancelButton.visibility = View.GONE
        deleteButton.visibility = View.GONE
        saveButton.visibility = View.GONE
    }

    fun activateEditMode() {
        editMode = true
        unsavedBlog=savedBlog
        blogTitle.inputType = 1  // // inputType= "text" (editable) : https://developer.android.com/reference/android/widget/TextView.html#attr_android:inputType
        blogDescription.inputType = 1
        editButton.visibility = View.GONE
        cancelButton.visibility = View.VISIBLE
        deleteButton.visibility = View.VISIBLE
        saveButton.visibility = View.VISIBLE
    }

    //BACK BUTTON:
    fun backToMain(view: View) {
        unsavedBlog = BlogEntity(0, "", "") //changes were not saved
        var intent = Intent()

        //AJUTINE:
        val db = LocalDbClient.getDatabase(applicationContext)!!
        var blog2Title = "title 3"
        var blog2Desc = "desc 3"
        var blog2 = BlogEntity(0, blog2Title, blog2Desc) //0 corresponds to 'no value', autogenerates id
        db?.getBlogDao()?.addBlog(blog2)


        setResult(2, intent) // setResult(resultCode, resultIntent)
        finish() //back to Main Activity
    }

    //ALERT DIALOGUE:
    fun showAlertDialog(msg : String) {
        val dialog = AlertDialogFragment()
        dialog.show(supportFragmentManager, msg)
        //to get a handle to the fragment: findFragmentByTag()
    }
    override fun onDialogPositiveClick(dialog: DialogFragment) {
        Log.i("RESULT", "Ok button pressed")
    }
    override fun onDialogNegativeClick(dialog: DialogFragment) {
        Log.i("RESULT", "Cancel button pressed")
    }

}