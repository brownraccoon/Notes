package com.vishal.notes.ui

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.vishal.notes.R
import com.vishal.notes.model.Note
import com.vishal.notes.model.NoteDatabase
import com.vishal.notes.viewmodel.NoteViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


class MainActivity : AppCompatActivity(), NotesAdapter.OnNoteItemClick ,NotesAdapter.OnNoteChecked{
    private val recyclerView: RecyclerView by lazy { findViewById(R.id.recyclerview) }
    private var notesAdapter: NotesAdapter? = null
    private var searchView: SearchView? = null
    private val noteViewModel: NoteViewModel by viewModels()
    private var notesList : ArrayList<Note>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initUI()
    }

    private fun initUI() {
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.addItemDecoration(
            DividerItemDecoration(
                this,
                DividerItemDecoration.VERTICAL
            )
        )

        noteViewModel.getAllNotes().observe(this, Observer<List<Note>> { notes ->
            notes?.let {
                notesList = notes as ArrayList<Note>
                notesAdapter = NotesAdapter(notes, this@MainActivity)
                recyclerView.adapter = notesAdapter
                notesAdapter!!.notifyDataSetChanged()
            }
        })


    }


    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.mainmenu, menu)

        val search = menu?.findItem(R.id.search_action)
        searchView = search?.actionView as SearchView
        searchView!!.isSubmitButtonEnabled = true
        searchView!!.setOnQueryTextListener(object : androidx.appcompat.widget.
        SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                if (query != null) {
                    getSearchResultForQuery(query)
                }
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                if (newText != null) {
                    getSearchResultForQuery(newText)
                }
                return true
            }

        })

        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id: Int = item.getItemId()

        if (id == R.id.add_action) {
            startActivity(Intent(this@MainActivity, CreateNoteActivity::class.java))
        }
        return super.onOptionsItemSelected(item)
    }

    private fun getSearchResultForQuery(term: String){
        var searchText = term
        searchText = "%$searchText%"

        noteViewModel.getSearchResult(term = searchText).observe(
            this@MainActivity,
            Observer { res ->
                res?.let {
                    notesAdapter = NotesAdapter(res, this@MainActivity)
                    recyclerView.adapter = notesAdapter
                    notesAdapter!!.notifyDataSetChanged()
                }

            })
    }

    override fun onNoteClick(pos: Int) {
        startActivity(
            Intent(
                this@MainActivity,
                CreateNoteActivity::class.java
            ).putExtra("note", notesList?.get(pos))
        )
    }

    override fun onNoteChecked(pos: Int,value:Boolean) {
        CoroutineScope(Dispatchers.IO).launch {
             val notechecked: Note = notesList?.get(pos)!!
            NoteDatabase.getInstance(this@MainActivity).noteDao().updateNote(
                notechecked.also { it.done=value }
            )
        }
    }
}