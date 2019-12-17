package com.example.travelblog

import android.content.Context
import androidx.room.Room
import com.example.travelblog.room.BlogDatabase

object LocalDbClient {
    var blogDb : BlogDatabase? = null //if database doesnt exist, is null
    fun getDatabase(context: Context) : BlogDatabase? {

        if (blogDb == null){
            blogDb = Room.databaseBuilder(
                context.applicationContext, BlogDatabase::class.java, "myBlog") //name- you can make multiple
                //instances of the DB schema (eg if DB is the same, but diff. users have unique instances  of the DB),
                .fallbackToDestructiveMigration() // each time schema changes, data is lost!
                .allowMainThreadQueries() // if possible, use background thread instead
                .build()
        }
        return blogDb
    }

    //FROM LAB VIDEO:  About .fallbackToDestructiveMigration()  :
    //if you modify Entity, but dont update version in DB, you'll get exception- therefore when
    //DB changes, you have to update DB to a newer version. Secondly, a proper approach would be to
    //also create migrations (how to carry over/transform data from prev. version to the next).
    //If you dont want to handle this, and you dont care if you blow up the whole DB and start over,
    //you can use the shortcut provided here. I.e. each time the schema changes, the data is lost.
    //In a real app., you should also handle migrations (in this lab we dont do it).

}



