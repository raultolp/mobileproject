package com.example.travelblog.room

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = arrayOf(BlogEntity::class, BlogItemEntity::class, BlogItemEntryEntity::class), version= 1)
abstract class BlogDatabase : RoomDatabase() {
    abstract fun getBlogDao() : BlogDao
}

//have to specify version, because each time you change sth in entities, you should do
// version management of the DB (Room is tracking different versions).


