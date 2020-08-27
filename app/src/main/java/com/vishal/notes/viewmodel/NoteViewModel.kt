package com.vishal.notes.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import com.vishal.notes.model.Note
import com.vishal.notes.repository.NoteRepository

class NoteViewModel(application: Application) : AndroidViewModel(application) {

    private var repository: NoteRepository =
        NoteRepository(application)
    private var allNotes: LiveData<List<Note>> = repository.getAllNotes()

    fun insertNote(note: Note) {
        repository.insertNote(note)
    }

    fun getAllNotes(): LiveData<List<Note>> {
        return allNotes
    }

    fun getSearchResult(term: String) : LiveData<List<Note>> {
        return repository.getSearchResult(term)
    }
}