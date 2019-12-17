package com.example.travelblog.room

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "blogItems")
data class BlogItemEntity(
    @PrimaryKey(autoGenerate = true) var blogItemId: Int, //autogenerate-> autoincrements the id
    var blogIdRef: Int?,  // ok
    var placeName: String?,
    var latitude: Double?,
    var longitude: Double?
)