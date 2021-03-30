package com.example.aniruddha.noteapp.viewModel

import android.app.Application
import android.util.Log
import androidx.lifecycle.*
import com.example.aniruddha.noteapp.dao.NoteDao
import com.example.aniruddha.noteapp.database.NoteRoomDatabase
import com.example.aniruddha.noteapp.entities.Note
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class NoteViewModel(application: Application) : AndroidViewModel(application) {

    private val TAG = this.javaClass.name
    private val noteDao : NoteDao
    private val noteDB : NoteRoomDatabase = NoteRoomDatabase.getDatabase(application)
    //private val mAllNotes : LiveData<List<Note>>
    private val mAllImportantNotes : LiveData<List<Note>>
    private val mAllCompleteNotes : LiveData<List<Note>>
    private val mAllInCompleteNotes : LiveData<List<Note>>

    val _searchStringLiveData = MutableLiveData<String>()
    init {
        noteDao = noteDB.noteDao()
        //mAllNotes = noteDao.getNotes()
        mAllImportantNotes = noteDao.getImportantNotes()
        mAllCompleteNotes = noteDao.getCompletedNotes()
        mAllInCompleteNotes = noteDao.getIncompleteNotes()
        _searchStringLiveData.value = ""
    }

    //searching code
    private val tasksData: LiveData<List<Note>> = Transformations.switchMap(_searchStringLiveData){
        noteDao.getNotes(_searchStringLiveData.value.orEmpty())
    }

    val tasks = tasksData

    // wrapper function
    fun insert(note : Note) = viewModelScope.launch(Dispatchers.IO){
        noteDao.insert(note)
    }

    // wrapper function
    fun update(note : Note) = viewModelScope.launch(Dispatchers.IO){
        noteDao.update(note)
    }

    // wrapper function
    fun getAllNotes(query: String) : LiveData<List<Note>>{
        return noteDao.getNotes("")
    }

    // wrapper function
    fun getAllImportantNotes() : LiveData<List<Note>>{
        return mAllImportantNotes
    }

    // wrapper function
    fun getAllCompletedNotes() : LiveData<List<Note>>{
        return mAllCompleteNotes
    }

    // wrapper function
    fun getAllInCompleteNotes() : LiveData<List<Note>>{
        return mAllInCompleteNotes
    }

    fun deleteCompleted() = viewModelScope.launch (Dispatchers.IO){
        noteDao.deleteAllCompleted()
    }

    // wrapper function
    fun delete(note : Note) = viewModelScope.launch(Dispatchers.IO){
        noteDao.delete(note)
    }

    fun onTaskComplete(note: Note, decider: Int) = viewModelScope.launch(Dispatchers.IO){
        noteDao.update(note.copy(isComplete = decider==2))
    }

    fun onTaskImportant(note: Note, decider: Int) = viewModelScope.launch(Dispatchers.IO){
        noteDao.update(note.copy(isImportant = decider==4))
    }

    // scope of view model ends when activity is destroyed completely
    override fun onCleared() {
        super.onCleared()
        Log.i(TAG, "ViewModel Destroyed")
    }
}