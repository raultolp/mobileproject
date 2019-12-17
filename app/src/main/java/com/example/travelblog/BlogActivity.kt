package com.example.travelblog

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import com.example.travelblog.room.BlogEntity
import kotlinx.android.synthetic.main.blog_activity.*


class BlogActivity: AppCompatActivity() {

    var editMode = true
    var selectedPlace = "" //name of selected place (blog item title)
    var blogName = ""
    var selectedPlaceIsNew = false
    var description = ""
    var blogId = 0
    var blog = BlogEntity(0, "", "")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.blog_activity)
        //setContentView(R.layout.activity_main)

        var db = LocalDbClient.getDatabase(applicationContext)
        //db?.getBlogDao()?.insertRecipes(myRecipe)

        //Getting values passed on through indents:
        editMode = intent.getBooleanExtra("edit mode", false)
        if (!editMode){
            deactivateEditMode()
            blogId = intent.getIntExtra("blog id", 0)
            blog= db?.getBlogDao()?.loadSingleBlog(blogId)!!
            //blogName = intent.getStringExtra("blog name")
            blogName = blog.blogTitle.toString()
            description = blog.blogDescription.toString()

            blogTitle.text = Editable.Factory.getInstance().newEditable(blogName)
            //description = "An amazing place to visit!"  //TODO: find description from database
            blogDescription.text = Editable.Factory.getInstance().newEditable(description)
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
        intent.putExtra("blogName", selectedPlace)
        intent.putExtra("placeName", blogName) //TODO: vb-olla peaks kaasa panema hoopis blogi id ja koha ID andmebaasis?
        //TODO: if blog title has not been not entered (for new blog), dont allow opening blog item (show notification)
        startActivity(intent)
    }

    //AJUTINE NUPP:
    fun onPlaceSelected(view: View) {
        //TODO: nupu vajutamise asemel rakenda seda funktsiooni, kui mapsile on uus marker pandud või see ära võetud
        //Seni on ajutine nupp vajalik, sest muidu ei saa edasi Blog Item Activity'sse liikuda

        selectedPlace ="New place"  // "Tartu"
        selectedPlaceIsNew = true  //TODO: decide if is new or not
        //TODO: save place name and coordinates if it's a new place

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
        //TODO: add content

        var intent = Intent()
        intent.putExtra("blogName", blogName)
        setResult(2, intent) // setResult(resultCode, resultIntent)
        finish() //back to Main Activity
    }

    //SAVE BUTTON:
    fun saveBlog(view: View) {
        //TODO: add content; dont allow saving two blogs with same name or with blog name being emty string
        deactivateEditMode()
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
        blogTitle.inputType = 1  // // inputType= "text" (editable) : https://developer.android.com/reference/android/widget/TextView.html#attr_android:inputType
        blogDescription.inputType = 1
        editButton.visibility = View.GONE
        cancelButton.visibility = View.VISIBLE
        deleteButton.visibility = View.VISIBLE
        saveButton.visibility = View.VISIBLE
    }

    //BACK BUTTON:
    fun backToMain(view: View) {
        var intent = Intent()
        intent.putExtra("blogName", blogName)
        setResult(2, intent) // setResult(resultCode, resultIntent)
        finish() //back to Main Activity
    }

}