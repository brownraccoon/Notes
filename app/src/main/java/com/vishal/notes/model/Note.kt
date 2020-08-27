package com.vishal.notes.model

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize

@Entity(tableName = "notes")
@Parcelize
data class Note(
    @PrimaryKey(autoGenerate = true)
     val note_id: Long? = 0,

    @ColumnInfo(name = "title")
     val title: String? = null,

    @ColumnInfo(name = "description")
     val description: String? = null,

    @ColumnInfo(name = "done")
    var done: Boolean? = false
): Parcelable
