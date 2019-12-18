package com.example.travelblog

import android.content.Intent
import android.graphics.BitmapFactory
import android.os.Bundle
import android.text.Editable
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.DialogFragment
import com.example.travelblog.room.BlogItemEntryEntity
import com.example.travelblog.room.BlogItemEntity
import com.example.travelblog.room.BlogEntity
import kotlinx.android.synthetic.main.blog_activity.*
import kotlinx.android.synthetic.main.blog_item_activity.*

class BlogItemActivity: AppCompatActivity(), AlertDialogFragment.AlertDialogListener {

    var editMode = true
    var place = "New place" //blog item title
    //var blogName = "" //if needed as identifier of the blog in database
    var isNewPlace = false
    var noPhoto = true
    var blogid = 0
    var itemid = 0
    var entries : MutableList<BlogItemEntryEntity> = mutableListOf()  //saved entries
    var currentEntryIndex = 0
    var savedEntry = BlogItemEntryEntity(0, 0, "", "", "", "")
    var unsavedEntry = BlogItemEntryEntity(0, 0, "", "", "", "")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.blog_item_activity)

        var db = LocalDbClient.getDatabase(applicationContext)

        //Getting values passed on through indents:
        editMode = intent.getBooleanExtra("editMode", false)  //kas vaja?
        //isNewPlace = intent.getBooleanExtra("isNewItem", false)
        blogid = intent.getIntExtra("blog id", blogid)
        itemid = intent.getIntExtra("blogitem id", itemid)

        var item = db?.getBlogDao()?.loaditem(itemid)!!
        if (item.placeName.equals("")){
            isNewPlace = true
        }

        if (!isNewPlace){  // if (!editMode)
            //TODO: get first image, date and time and description from database
            place = item.placeName!!
            noPhoto = false
            deactivateItemEditMode()
            //entries:
            entries = db?.getBlogDao()?.loadEntries(itemid).toMutableList()
            if (entries!=null && !entries.isEmpty()){
                savedEntry = entries.get(0)
                dateTimeField.text = Editable.Factory.getInstance().newEditable(savedEntry.date + "    " + savedEntry.time)
                blogItemDescription.text = Editable.Factory.getInstance().newEditable(savedEntry.photoDescription)
                if (entries.size==1){
                    prevButton.visibility = View.GONE
                } else {
                    prevButton.visibility = View.VISIBLE
                }
            }
        } else {
            noPhoto = true
            place = "New place"
            activateItemEditMode()
            nextButton.visibility = View.GONE
        }
        prevButton.visibility = View.GONE
        blogItemTitle.text = Editable.Factory.getInstance().newEditable(place)
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
        //TODO: replace image with the "nophoto.png image in drawable folder)
        //imageView.setImageBitmap("@drawable/nophoto")
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
    fun deleteEntry(view: View) {
 /*       var db = LocalDbClient.getDatabase(applicationContext)
        if (unsavedEntry.blogItemEntryId!=0) {  //if entry has been saved
            if (entries.size==1){  // if current entry is the only entry

            }
        } else {  //entry has not been saved
            unsavedEntry = BlogItemEntryEntity(0, 0, "", "", "", "")
            if (entries.size==0) {  //unsaved and only entry

            } else {

            }
        }

        // if current entry is the only entry:
        if (entries.size==1 && unsavedEntry.blogItemEntryId!=0) {

        }
        //if current entry is first entry:
        else if(){

        }
        //if current entry is last entry:
        else if(){

        } else {

        }*/

        //if current entry


        //delete current blog item entry (photo, description), and
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
        blogItemTitle.text = Editable.Factory.getInstance().newEditable(place)  //dont save new place
        dateTimeField.text = Editable.Factory.getInstance().newEditable(savedEntry.date + "    " + savedEntry.time)
        blogItemDescription.text = Editable.Factory.getInstance().newEditable(savedEntry.photoDescription)
        //TODO: cancel unsaved image (replace with the "nophoto.png image in drawable folder)
        //imageView.setImageBitmap("@drawable/nophoto")
        unsavedEntry = BlogItemEntryEntity(0, 0, "", "", "", "")
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
        unsavedEntry = savedEntry
        blogItemTitle.inputType = 1  // // inputType= "text" (editable) : https://developer.android.com/reference/android/widget/TextView.html#attr_android:inputType
        blogItemDescription.inputType = 1
        editButton2.visibility = View.GONE
        cancelButton2.visibility = View.VISIBLE
        deleteButton2.visibility = View.VISIBLE
        saveButton2.visibility = View.VISIBLE
        updateImageButtons()
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

    //BACK BUTTON:
    fun backToBlog(view: View) {
        unsavedEntry = BlogItemEntryEntity(0, 0, "", "", "", "")
        finish()
    }

}
