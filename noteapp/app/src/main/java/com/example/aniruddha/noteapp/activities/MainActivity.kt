package com.example.aniruddha.noteapp.activities

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.example.aniruddha.noteapp.R
import com.example.aniruddha.noteapp.adapter.NoteListAdapter
import com.example.aniruddha.noteapp.entities.Note
import com.example.aniruddha.noteapp.listeners.NoteListener
import com.example.aniruddha.noteapp.util.onQueryTextChanged
import com.example.aniruddha.noteapp.viewModel.NoteViewModel
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.content_main.*
import java.io.Serializable
import java.util.*


class MainActivity : AppCompatActivity(), NoteListener {
    private val TAG = this.javaClass.name
    private lateinit var noteViewModel: NoteViewModel
    private lateinit var noteListAdapter: NoteListAdapter
    private var noteClickedPosition = -1
    private var flag = true

    companion object {
        private const val NEW_NOTE_ACTIVITY_REQUEST_CODE = 1
        private const val UPDATE_NOTE_ACTIVITY_REQUEST_CODE = 2
        val SHARED_PREF = "sharedPrefs"
        val TEXT = "text"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // toolbar
        setSupportActionBar(toolbarCustom)

        // recycler view
        noteListAdapter = NoteListAdapter(this, this)
        recyclerView.adapter = noteListAdapter
        loadData()
        setLayoutManager()

        // floating action button onClick method
        newNote.setOnClickListener {
            val intent = Intent(this, NewNoteActivity::class.java)
            startActivityForResult(intent, NEW_NOTE_ACTIVITY_REQUEST_CODE)
        }

        // attaching the viewModel
        noteViewModel = ViewModelProvider(this, ViewModelProvider.AndroidViewModelFactory.getInstance(application)).get(NoteViewModel::class.java)

        noteViewModel.tasks.observe(this, Observer { list ->
            list?.let {
                noteListAdapter.setNote(it)
            }
        })

        // for deleting on swipe
        ItemTouchHelper(
                object : ItemTouchHelper.SimpleCallback(0,
                        ItemTouchHelper.RIGHT) {
                    override fun onMove(recyclerView: RecyclerView,
                                        viewHolder: ViewHolder, target: ViewHolder): Boolean {
                        // move item in `fromPos` to `toPos` in adapter.
                        return false // true if moved, false otherwise
                    }

                    override fun onSwiped(viewHolder: ViewHolder, direction: Int) {
                        // remove from adapter
                        noteViewModel.delete(noteListAdapter.getNoteAt(viewHolder.adapterPosition))
                    }
                }).attachToRecyclerView(recyclerView)

    }

    // getting the data back from NewNoteActivity updating or creating a new note
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if(requestCode == NEW_NOTE_ACTIVITY_REQUEST_CODE && resultCode == RESULT_OK){

            //code to insert the note title
            val noteId : String = UUID.randomUUID().toString()
            val color = data?.getStringExtra(NewNoteActivity.NOTE_COLOR)
            val title = data?.getStringExtra(NewNoteActivity.NOTE_ADDED)
            val subTitle = data?.getStringExtra(NewNoteActivity.NOTE_SUBTITLE)
            val description = data?.getStringExtra(NewNoteActivity.NOTE_DESCRIPTION)
            val date = data?.getStringExtra(NewNoteActivity.NOTE_DATE)
            val important = data?.getBooleanExtra(NewNoteActivity.NOTE_IMPORTANT, false)
            val complete = data?.getBooleanExtra(NewNoteActivity.NOTE_COMPLETE, false)
            val note = Note(noteId, title!!, color!!, important == true, complete == true, date.toString(), subTitle.toString(), description.toString())
            noteViewModel.insert(note)

            //Toast.makeText(this, getString(R.string.saved), Toast.LENGTH_LONG).show()
        }
        else if(requestCode == UPDATE_NOTE_ACTIVITY_REQUEST_CODE && resultCode == RESULT_OK){

            //code to update the note title

            val r = data?.getIntExtra(NewNoteActivity.RESULT_DELETE, 0)
            var noteId : String? = data?.getStringExtra(NewNoteActivity.NOTE_ID)
            val color = data?.getStringExtra(NewNoteActivity.NOTE_COLOR)
            val title = data?.getStringExtra(NewNoteActivity.NOTE_ADDED)
            val important: Boolean? = data?.getBooleanExtra(NewNoteActivity.NOTE_IMPORTANT, false)
            if(noteId == null){
                Toast.makeText(this,"new id", Toast.LENGTH_LONG).show()
                noteId = UUID.randomUUID().toString()
            }
            val complete: Boolean? = data?.getBooleanExtra(NewNoteActivity.NOTE_COMPLETE, false)
            val subTitle = data?.getStringExtra(NewNoteActivity.NOTE_SUBTITLE)
            val description = data?.getStringExtra(NewNoteActivity.NOTE_DESCRIPTION)
            val date = data?.getStringExtra(NewNoteActivity.NOTE_DATE)
            var i : Boolean = important!!

            Toast.makeText(this, "Marked completed" + i.toString(), Toast.LENGTH_SHORT).show()
            val note = Note(noteId, title!!, color!!, i, (complete == true), date.toString(), subTitle.toString(), description.toString())
            if(r == 0)
                noteViewModel.update(note)
            else if(r == 2 || r == 3){
                noteViewModel.onTaskComplete(note, r)
            }
            else if(r == 4 || r == 5){
                noteViewModel.onTaskImportant(note, r)
            }
            else{
                noteViewModel.delete(note)
            }

            //Toast.makeText(this, getString(R.string.updated), Toast.LENGTH_LONG).show()
        }
        else{
            Toast.makeText(this, "Note not saved", Toast.LENGTH_LONG).show()
        }
    }



    // menu is inflated
    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater : MenuInflater = menuInflater
        inflater.inflate(R.menu.top_app_bar_notes, menu)
        val searchItem = menu.findItem(R.id.search)
        val searchView = searchItem.actionView as androidx.appcompat.widget.SearchView

        searchView.onQueryTextChanged{
            // update search query
            noteViewModel._searchStringLiveData.value = it
        }
        return true
    }

    // if selected
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when(item.itemId){
            R.id.change -> {
                flag = !flag
                setLayoutManager()
                true
            }
            R.id.important -> {

                true
            }
            R.id.complete -> {
                noteViewModel.deleteCompleted()
                true
            }
            R.id.all -> {
                noteViewModel.getAllNotes("").observe(this, Observer { list ->
                    list?.let {
                        noteListAdapter.setNote(it)
                    }
                })
                true
            }
            else -> super.onOptionsItemSelected(item)
        }

    }

    private fun setLayoutManager(){
        if(flag) {
            recyclerView.layoutManager = LinearLayoutManager(this)
        }
        else{
            recyclerView.layoutManager = StaggeredGridLayoutManager(2, LinearLayoutManager.VERTICAL)
        }
        saveData()
    }

    override fun onNoteClicked(note: Note, position: Int) {
        noteClickedPosition = position
        val intent = Intent(this, NewNoteActivity::class.java)
        intent.putExtra("isViewOrUpdate", true)
        intent.putExtra("note", note)
        startActivityForResult(intent, UPDATE_NOTE_ACTIVITY_REQUEST_CODE)
    }

    //saving the data
    private fun saveData(){
        val sharedPreferences: SharedPreferences = getSharedPreferences(SHARED_PREF, MODE_PRIVATE)
        val editor: SharedPreferences.Editor = sharedPreferences.edit()
        editor.putBoolean(TEXT, flag)
        editor.apply()
    }

    private fun loadData(){
        val sharedPreferences: SharedPreferences = getSharedPreferences(SHARED_PREF, MODE_PRIVATE)
        flag = sharedPreferences.getBoolean(TEXT, true)
    }
}