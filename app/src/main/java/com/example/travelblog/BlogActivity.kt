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
import com.example.travelblog.room.BlogItemEntryEntity
import kotlinx.android.synthetic.main.blog_activity.*
import com.example.travelblog.LocalDbClient


class BlogActivity: AppCompatActivity(), AlertDialogFragment.AlertDialogListener {

    var editMode = true
    var selectedPlace = "" //name of selected place (blog item title)
    var selectedPlaceIsNew = true
    var savedBlog = BlogEntity(0, "", "")
    var unsavedBlog = BlogEntity(0, "", "")
    var selectedItem = BlogItemEntity(0, 0, "New place", 0.0, 0.0)

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
        } else {
            activateEditMode()
        }
    }


    //AJUTINE NUPP:
    fun onPlaceSelected(view: View) {
        //TODO: nupu vajutamise asemel rakenda seda funktsiooni, kui mapsile on uus marker pandud või see ära võetud
        //Seni on ajutine nupp vajalik, sest muidu ei saa edasi Blog Item Activity'sse liikuda
        //TODO: dont allow selecting more than one new location
        //TODO: Selecting new location (putting new marker on map) is only possible in Edit mode

        selectedPlace ="New place"  // "Tartu"  //TODO: change place name if existing marker was selected
        selectedPlaceIsNew = true  //TODO: change to false if not new place
/*        if (!editMode){
            selectedPlaceIsNew = false
        }*/

        var lat = 58.385156  //TODO: get coordinates of selected marker from map
        var lon = 26.725108

        var blogid = savedBlog.blogId
        var itemid : Int
        if (selectedPlaceIsNew){
            selectedItem = BlogItemEntity(0, blogid, selectedPlace, lat, lon)
        } else {
            selectedItem = selectedItem  //TODO: replace: get BlogItemEntity from the list also used for the map
        }

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
        var db = LocalDbClient.getDatabase(applicationContext)

        if (unsavedBlog.blogId!=0){  //if blog has been saved
            var itemIds= db?.getBlogDao()?.loadSpecificBlogItemIds(unsavedBlog.blogId)
            if (itemIds!=null && !itemIds.isEmpty()){  //if blog has items
                for (itemid in itemIds){
                    var entityIds= db?.getBlogDao()?.loadEntryIds(itemid)
                    if (entityIds!=null && !entityIds.isEmpty()) {  //if item has entries
                        for (entityid in entityIds){
                            db?.getBlogDao()?.deleteBlogItemEntry(db?.getBlogDao()?.loadEntry(entityid)!!)
                        }
                    }
                    db?.getBlogDao()?.deleteBlogItem(db?.getBlogDao()?.loaditem(itemid)!!)
                }
            }
            db?.getBlogDao()?.deleteBlog(db?.getBlogDao()?.loadSingleBlog(unsavedBlog.blogId)!!)
        }
        unsavedBlog = BlogEntity(0, "", "") //changes were not saved
        selectedItem = BlogItemEntity(0, 0, "New place", 0.0, 0.0)

        var intent = Intent()
        //setResult(2, intent) // setResult(resultCode, resultIntent)
        finish() //back to Main Activity
    }


    //OPEN BUTTON:
    fun openBlogItem(view: View) {
        //dont allow opening blog item if blog has not been saved (Open cannot be done in Edit mode):
/*        if (editMode){
            var newTitle = unsavedBlog.blogTitle
            var newDesc = unsavedBlog.blogDescription
            if (!savedBlog.blogTitle.equals(newTitle) || !savedBlog.blogDescription.equals(newDesc)){
                showAlertDialog("Save changes first!")
            } else if (selectedPlaceIsNew) {
            //else if (selectedItem.blogItemId==0 && selectedItem.latitude!=0.0) {
                showAlertDialog("Save changes first!")
            }*/
        if (editMode){
            showAlertDialog("Save changes first!")
        } else {
            var blogid: Int
            var itemid: Int
            if (editMode) {
                blogid = unsavedBlog.blogId
            } else {
                blogid = savedBlog.blogId
            }
            itemid = selectedItem.blogItemId  //check
            val intent = Intent(this, BlogItemActivity::class.java)
            intent.putExtra("editMode", editMode)
            intent.putExtra("isNewItem", selectedPlaceIsNew)
            intent.putExtra("blog id", blogid)
            intent.putExtra("blogitem id", itemid)
            startActivityForResult(intent, 3)
        }
    }

    //RETURNING FROM BLOG ITEM ACTIVITY:
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode==3 && resultCode==4){
            //model.refresh()  //TODO:  kas vaja?
        }
    }

    //CANCEL BUTTON:
    fun cancelInputs(view: View) {
        //selectedItem = BlogItemEntity(0, 0, "New place", 0.0, 0.0)
        unsavedBlog = BlogEntity(0, "", "")
        blogDescription.text = Editable.Factory.getInstance().newEditable(savedBlog.blogDescription)
        blogTitle.text = Editable.Factory.getInstance().newEditable(savedBlog.blogTitle)
        deactivateEditMode()
    }

    //SAVE BUTTON:
    fun saveBlog(view: View) {
        var db = LocalDbClient.getDatabase(applicationContext)
        var title = blogTitle.text.toString()
        var desc = blogDescription.text.toString()

        //dont allow saving with blog name being empty string:
        if (title.equals("")){
            showAlertDialog("Blog with no title cannot be saved.")
        } else {
            //SAVING NEW BLOG:
            //dont allow saving two blogs with the same name (for new blog):
            if (savedBlog.blogId==0){
                var allTitles = db?.getBlogDao()?.loadBlogTitles()
                if (allTitles!=null && allTitles.size>0 && allTitles.contains(title)){
                    showAlertDialog("Blog with this name already exists.")
                } else {
                    //saving new blog (also find id of new blog, update blogid saved/unsaved blog):
                    unsavedBlog.blogDescription = desc
                    unsavedBlog.blogTitle = title
                    var oldIds= db?.getBlogDao()?.loadBlogIds()
                    db?.getBlogDao()?.addBlog(unsavedBlog)
                    var newIds= db?.getBlogDao()?.loadBlogIds()
                    var newId : Int
                    if (oldIds==null || oldIds.size==0){
                        newId = newIds!!.get(0)
                    } else {
                        newId = findId(oldIds, newIds!!)
                    }
                    unsavedBlog.blogId = newId
                    savedBlog=unsavedBlog

                    //saving also new marker's location (if a marker has been placed on map):
                    if(selectedItem.latitude!=0.0){
                        var oldIds= db?.getBlogDao()?.loadBlogItemIds()
                        db?.getBlogDao()?.addBlogItem(selectedItem)
                        var newIds= db?.getBlogDao()?.loadBlogItemIds()
                        var newId : Int
                        if (oldIds==null || oldIds.isEmpty()){
                            newId = newIds!!.get(0)
                        } else {
                            newId = findId(oldIds, newIds!!)
                        }
                        selectedItem.blogItemId = newId
                        //selectedPlaceIsNew = false
                    }
                }
            } else {
                //SAVING (UPDATING) EXISTING BLOG:
                unsavedBlog.blogDescription = desc
                unsavedBlog.blogTitle = title
                db?.getBlogDao()?.updateBlog(unsavedBlog)
                savedBlog=unsavedBlog
                //saving also new marker's location (if a marker has been placed on map):
                if(selectedItem.latitude!=0.0 && selectedItem.blogItemId==0){
                    var oldIds= db?.getBlogDao()?.loadBlogItemIds()
                    db?.getBlogDao()?.addBlogItem(selectedItem)
                    var newIds= db?.getBlogDao()?.loadBlogItemIds()
                    var newId : Int
                    if (oldIds==null || oldIds.isEmpty()){
                        newId = newIds!!.get(0)
                    } else {
                        newId = findId(oldIds, newIds!!)
                    }
                    selectedItem.blogItemId = newId
                    //selectedPlaceIsNew = false
                }
            }
        }
        deactivateEditMode()
    }

    fun findId(old : Array<Int>, new : Array<Int>): Int {
        var id = 0
        for (x in new){
            if (!old.contains(x)){
                id = x
                break
            }
        }
        return id
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
        //openItemButton.visibility = View.GONE
    }

    //BACK BUTTON:
    fun backToMain(view: View) {
        unsavedBlog = BlogEntity(0, "", "") //changes were not saved
        var intent = Intent()

/*
        //AJUTINE:
        val db = LocalDbClient.getDatabase(applicationContext)!!
        var blog2Title = "title 3"
        var blog2Desc = "desc 3"
        var blog2 = BlogEntity(0, blog2Title, blog2Desc) //0 corresponds to 'no value', autogenerates id
        db?.getBlogDao()?.addBlog(blog2)
*/


        //setResult(2, intent) // setResult(resultCode, resultIntent)
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