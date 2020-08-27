package com.vishal.notes.repository

import android.app.Application
import androidx.lifecycle.LiveData
import com.vishal.notes.model.Note
import com.vishal.notes.model.NoteDao
import com.vishal.notes.model.NoteDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.launch

class NoteRepository(application: Application) {

    private var noteDao: NoteDao

    private var allNotes: LiveData<List<Note>>

    init {
        val database: NoteDatabase = NoteDatabase.getInstance(
            application.applicationContext
        )!!
        noteDao = database.noteDao()
        allNotes = noteDao.getAllNotes()
    }

    fun insertNote(note: Note) {
      insertNoteInBackground(note)
    }


    fun getAllNotes(): LiveData<List<Note>> {
        return allNotes
    }

    fun getSearchResult(term:String): LiveData<List<Note>> {
        return noteDao.getSearchResult(term)
    }

    private fun insertNoteInBackground(note: Note) {
        CoroutineScope(IO).launch { noteDao.insertNote(note) }
    }

}