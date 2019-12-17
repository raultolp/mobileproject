package com.example.travelblog

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.example.travelblog.room.BlogEntity
import com.example.travelblog.room.BlogDatabase

//FROM LAB VIDEO:
// Extends not just ViewModel shown in lecture, but AndroidViewModel; the difference is that
// AndroidViewModel also has link to the application context. This means you can start a new
// activity/service, but also access databases.

class BlogViewModel(application: Application) : AndroidViewModel(application) {

    //currently holds some placeholder values in an Array:
    var entityArray : Array<BlogEntity> = arrayOf()
    var db : BlogDatabase

    //constructor in Kotlin (called when object is created):
    init {
        db = LocalDbClient.getDatabase(application.applicationContext)!!
    }

    fun refresh(){
        // Reload dataset from DB, put it in in-memory list (entityArray)
        //db.getRecipesDao().loadRecipeTitles()
        entityArray = db.getBlogDao().loadBlogs() //put it in the array the adapter is using
    }
}