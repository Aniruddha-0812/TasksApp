package com.example.aniruddha.noteapp.listeners

import com.example.aniruddha.noteapp.entities.Note

interface NoteListener {
    fun onNoteClicked(note: Note, position: Int)
}