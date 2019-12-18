package com.example.travelblog

import android.content.Intent
import android.graphics.BitmapFactory
import android.os.Bundle
import android.text.Editable
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.example.travelblog.room.BlogItemEntryEntity
import kotlinx.android.synthetic.main.blog_activity.*
import kotlinx.android.synthetic.main.blog_item_activity.*

class BlogItemActivity: AppCompatActivity() {

    var editMode = true
    var placeName = "" //blog item title
    //var blogName = "" //if needed as identifier of the blog in database
    var isNewPlace = false
    var noPhoto = true
    var blogid = 0
    var itemid = 0
    var entries : ArrayList<BlogItemEntryEntity> = arrayListOf()
    var currentEntryIndex = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.blog_item_activity)

        var db = LocalDbClient.getDatabase(applicationContext)

        //Getting values passed on through indents:
        editMode = intent.getBooleanExtra("editMode", false)
        isNewPlace = intent.getBooleanExtra("isNewItem", false)
        blogid = intent.getIntExtra("blog id", blogid)
        itemid = intent.getIntExtra("blogitem id", itemid)

        if (!isNewPlace){
            blogItemTitle.text = Editable.Factory.getInstance().newEditable(placeName)
            //TODO: get first image, date and time and description from database
            noPhoto = false
        } else {
            noPhoto = true
        }
        //placeName = db?.getBlogDao()?.loadPlaceName(itemid)!!   //TODO: FIND BUG!
        //blogName = intent.getStringExtra("blogName")
        if (!editMode){
            deactivateItemEditMode()
        } else {
            activateItemEditMode()
        }
        updateImageButtons()

    }

    //CAMERA BUTTON:
    fun openCamera(view: View) {
        //TODO: add content
        val intent = Intent(this, CameraActivity::class.java)
        MainActivity.mPhoto = null
        startActivity(intent)
        if (MainActivity.mPhoto === null) {
            noPhoto = true
        } else {
            noPhoto = false
            imageView.setImageBitmap(BitmapFactory.decodeFile(MainActivity.mPhoto.toString()))
        }
        updateImageButtons()
    }

    //IMAGE UPLOAD BUTTON
    fun fileUpload(view: View) {
        //TODO: add content
        noPhoto = false
        updateImageButtons()
    }

    //IMAGE DELETE BUTTON
    fun deleteImage(view: View) {
        //TODO: add content
        noPhoto = true
        updateImageButtons()
    }

    fun updateImageButtons(){
        if (!noPhoto){
            imageDeleteButton.visibility = View.VISIBLE
            cameraButton.visibility = View.GONE
            uploadButton.visibility = View.GONE
        } else {
            imageDeleteButton.visibility = View.GONE
            cameraButton.visibility = View.VISIBLE
            uploadButton.visibility = View.VISIBLE
        }
    }

    //PREVIOUS BUTTON
    fun openPreviousImage(view: View) {
        //TODO: add content
    }

    //NEXT BUTTON
    fun openNextImage(view: View) {
        //TODO: add content
    }

    //TODO: handle visibility of Previous and Next button

    //DELETE BUTTON:
    fun deletePlace(view: View) {
        //TODO: add content; deletes the current blog item entry (photo, description), and
        //opens previous (or next) entry. Only when the last entry is deleted, the Blog Item
        //itself is deleted as well.
        //If unsaved new entry is deleted, this equals to pushing cancel button.
        finish() //back to Main Activity
    }

    //SAVE BUTTON:
    fun savePlace(view: View) {
        //TODO: add content; dont allow saving two places with same name, or saving entry without having uploaded a photo
        deactivateItemEditMode()
    }

    //CANCEL BUTTON:
    fun cancelItemInputs(view: View) {
        //TODO:  add content
        // (initial data (data when edit mode is activated) should be saved in variables, and restored if Cancel is pressed)
        deactivateItemEditMode()
    }

    //EDIT BUTTON:
    fun editItemInputs(view: View) {
        activateItemEditMode()
    }

    fun deactivateItemEditMode() {
        editMode = false
        blogItemTitle.inputType = 0  // // inputType= "none" (not editable) : https://developer.android.com/reference/android/widget/TextView.html#attr_android:inputType
        blogItemDescription.inputType = 0
        editButton2.visibility = View.VISIBLE
        cancelButton2.visibility = View.GONE
        deleteButton2.visibility = View.GONE
        saveButton2.visibility = View.GONE
        cameraButton.visibility = View.GONE
        uploadButton.visibility = View.GONE
        imageDeleteButton.visibility = View.GONE
    }

    fun activateItemEditMode() {
        editMode = true
        blogItemTitle.inputType = 1  // // inputType= "text" (editable) : https://developer.android.com/reference/android/widget/TextView.html#attr_android:inputType
        blogItemDescription.inputType = 1
        editButton2.visibility = View.GONE
        cancelButton2.visibility = View.VISIBLE
        deleteButton2.visibility = View.VISIBLE
        saveButton2.visibility = View.VISIBLE
        updateImageButtons()
    }

    //BACK BUTTON:
    fun backToBlog(view: View) {
        finish()
    }

}
