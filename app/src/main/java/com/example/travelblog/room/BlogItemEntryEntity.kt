package com.example.travelblog.room

import android.os.Bundle
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.LocalDate
import java.time.LocalDateTime

@Entity(tableName = "blogItemEntries")
data class BlogItemEntryEntity(
    @PrimaryKey(autoGenerate = true) var blogItemEntryId: Int, //autogenerate-> autoincrements the id
    var blogItemIdRef: Int?,  //ok
    var photoDescription: String?,
    var date: String?,
    var time: String?,
    var photo: String?  //path to file (cannot store Bundle in database)
    )

//NOTE:
//SQLite does not have a storage class for storing dates and/or times. Instead, the built-in
// Date and Time Functions of SQLite are capable of storing dates and times as TEXT, REAL,
// or INTEGER values: ... https://androidkt.com/datetime-datatype-sqlite-using-room/

//DATETIME: see https://www.baeldung.com/java-8-date-time-intro