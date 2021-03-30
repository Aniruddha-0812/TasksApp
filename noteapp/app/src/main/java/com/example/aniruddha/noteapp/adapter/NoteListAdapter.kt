package com.example.aniruddha.noteapp.adapter

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.GradientDrawable
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.example.aniruddha.noteapp.R
import com.example.aniruddha.noteapp.entities.Note
import com.example.aniruddha.noteapp.listeners.NoteListener
import java.util.*
import java.util.logging.Handler
import kotlin.collections.ArrayList

class NoteListAdapter(context: Context, noteListener: NoteListener) : RecyclerView.Adapter<NoteListAdapter.NoteViewHolder>() {

    private val layoutInflater : LayoutInflater = LayoutInflater.from(context)
    private  var mNotes : List<Note>? = null
    private  var mNotesSource : List<Note>? = null
    private var noteListner: NoteListener

    init {
        mNotesSource = mNotes
        noteListner = noteListener
    }

    inner class NoteViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val noteTitleView : TextView = itemView.findViewById(R.id.displayNoteTitle)
        val date : TextView = itemView.findViewById(R.id.displayNoteDate)
        val noteSubTitleView : TextView = itemView.findViewById(R.id.displayNoteSubTitle)
        val noteDescriptionView : TextView = itemView.findViewById(R.id.displayNoteDescription)
        val noteLayout : CardView = itemView.findViewById(R.id.layoutNote)
        val starPresent : View = itemView.findViewById(R.id.star)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NoteViewHolder {
        val itemView = layoutInflater.inflate(R.layout.list_item, parent, false)
        return NoteViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: NoteViewHolder, position: Int) {
        if (mNotes != null) {
            val note = mNotes!![position]
            holder.noteTitleView.text = note.title

            holder.noteTitleView.paint.isStrikeThruText = note.isComplete

            if(note.subTitle.isEmpty()){
                holder.noteSubTitleView.visibility = View.GONE
            }else{
                holder.noteSubTitleView.visibility = View.VISIBLE
                holder.noteSubTitleView.text = note.subTitle
                holder.noteSubTitleView.paint.isStrikeThruText = note.isComplete
            }

            if(note.description.isEmpty()){
                holder.noteDescriptionView.visibility = View.GONE
            }else{
                holder.noteDescriptionView.visibility = View.VISIBLE
                holder.noteDescriptionView.text = note.description
                holder.noteDescriptionView.paint.isStrikeThruText = note.isComplete
            }
            holder.date.text = note.date
            holder.noteLayout.setCardBackgroundColor(Color.parseColor(note.colorNoteSpecific))

            if(note.isImportant){
                holder.starPresent.visibility = View.VISIBLE
            }else{
                holder.starPresent.visibility = View.GONE
            }

            holder.noteLayout.setOnClickListener {
                noteListner.onNoteClicked(mNotes!![position], position)
            }
        }
    }

    override fun getItemCount(): Int = if (mNotes == null){
        0
    }
    else{
        mNotes!!.size
    }

    // updating the notes
    fun setNote(notes : List<Note>){
        mNotes = notes
        notifyDataSetChanged()
    }

    fun getNoteAt(position: Int): Note {
        return mNotes!![position]
    }
}