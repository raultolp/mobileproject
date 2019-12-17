package com.example.travelblog.room

import androidx.room.Entity
import androidx.room.PrimaryKey

//a placeholder class for Recipes (Room database entity class)

@Entity(tableName = "blogs") //by default, the name of the table is the name of the class;
//adding this argument changes the default name.
data class BlogEntity(
    @PrimaryKey(autoGenerate = true) var blogId: Int, //autogenerate-> autoincrements the id
    var blogTitle : String?,
    var blogDescription: String?
)

