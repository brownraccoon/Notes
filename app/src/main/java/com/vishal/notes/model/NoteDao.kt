package com.vishal.notes.model

import androidx.lifecycle.LiveData
import androidx.room.*


@Dao
interface NoteDao {

    @Query("SELECT * FROM notes  ORDER BY LOWER(done) ASC ")
    fun getAllNotes(): LiveData<List<Note>>

    @Query("SELECT * FROM notes where description like :term")
    fun getSearchResult(term:String): LiveData<List<Note>>

    @Insert
    suspend fun insertNote(note: Note)

    @Update
    suspend fun updateNote(repos: Note)

}