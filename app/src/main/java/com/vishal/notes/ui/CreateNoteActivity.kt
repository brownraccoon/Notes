package com.vishal.notes.ui

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.vishal.notes.R
import com.vishal.notes.model.Note
import com.vishal.notes.model.NoteDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.launch

class CreateNoteActivity : AppCompatActivity() {
    private val saveButton: Button by lazy { findViewById(R.id.save_btn) }
    private val cancelButton: Button by lazy { findViewById(R.id.cancel_btn) }
    private val titleEditText: EditText by lazy { findViewById(R.id.title_et) }
    private val descriptionEditText: EditText by lazy { findViewById(R.id.description_et) }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_note)
        val data = intent.extras
        if (data!= null) {
            supportActionBar!!.setTitle(getString(R.string.view_note))
            val openedNote:Note?=data.getParcelable("note")
            titleEditText.setText(openedNote?.title)
            descriptionEditText.setText(openedNote?.description)
        }else{
            supportActionBar!!.setTitle(getString(R.string.create_note))
        }
        initUI()
    }

    private fun initUI() {
        saveButton.setOnClickListener{
            CoroutineScope(IO).launch {
                NoteDatabase.getInstance(this@CreateNoteActivity).noteDao().insertNote(
                    Note(
                        null,
                        titleEditText.text.toString(),
                        descriptionEditText.text.toString(),
                        false
                    )
                );
            }
            Toast.makeText(this, getString(R.string.note_saved), Toast.LENGTH_SHORT).show()
            finish()
        }
        cancelButton.setOnClickListener{
            finish()
        }
    }


}