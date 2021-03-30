package com.example.aniruddha.noteapp.util

import androidx.appcompat.widget.SearchView

inline fun SearchView.onQueryTextChanged(crossinline  listner: (String) -> Unit){
    this.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
        override fun onQueryTextSubmit(query: String?): Boolean {
            return true
        }

        override fun onQueryTextChange(newText: String?): Boolean {
            listner(newText.orEmpty())
            return true
        }
    })
}