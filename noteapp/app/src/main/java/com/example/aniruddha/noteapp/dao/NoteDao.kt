package com.example.aniruddha.noteapp.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.aniruddha.noteapp.entities.Note

@Dao
interface NoteDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(note: Note)

    @Update
    suspend fun update(note: Note)

    @Query("SELECT * FROM notes WHERE title LIKE '%' || :searchQuery || '%' ORDER BY date DESC")
    fun getNotes(searchQuery: String) : LiveData<List<Note>>

    @Query("SELECT * FROM notes WHERE important = 1")
    fun getImportantNotes() : LiveData<List<Note>>

    @Query("SELECT * FROM notes WHERE complete = 1")
    fun getCompletedNotes() : LiveData<List<Note>>

    @Query("SELECT * FROM notes WHERE complete = 0")
    fun getIncompleteNotes() : LiveData<List<Note>>

    @Query("DELETE FROM notes WHERE complete = 1")
    fun deleteAllCompleted()

    @Delete
    suspend fun delete(note: Note)
}