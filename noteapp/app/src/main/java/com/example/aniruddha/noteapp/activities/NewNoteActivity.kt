package com.example.aniruddha.noteapp.activities

import android.content.Intent
import android.content.res.ColorStateList
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.GradientDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import com.example.aniruddha.noteapp.R
import com.example.aniruddha.noteapp.entities.Note
import com.google.android.material.bottomsheet.BottomSheetBehavior
import kotlinx.android.synthetic.main.activity_new_note.*
import kotlinx.android.synthetic.main.layout_miscellaneous.*
import java.text.SimpleDateFormat
import java.util.*

class NewNoteActivity : AppCompatActivity(), View.OnClickListener {

    companion object{
        val NOTE_ADDED : String = "new_note"
        val NOTE_SUBTITLE : String = "note_subtitle"
        val NOTE_DESCRIPTION : String = "note_description"
        var NOTE_COLOR : String = "note_color"
        val NOTE_DELETE : String = "note_delete"
        val NOTE_IMPORTANT : String = "note_important"
        val NOTE_COMPLETE : String = "note_important"
        val NOTE_DATE : String = "note_date"
        val NOTE_ID : String = "note_id"
        val RESULT_DELETE : String = "delete"
    }

    private lateinit var selectedNoteColor : String
    private lateinit var viewSubtitleIndicator : View
    private var noteAlreadyAvailable : Note? = null
    private var importantval : Boolean = false
    private var complete : Boolean = false
    private lateinit var dateIndian : String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_new_note)



        viewSubtitleIndicator = findViewById(R.id.viewSubtitleIndicator)

        // back button
        setSupportActionBar(newNoteToolBar)
        newNoteToolBar.setNavigationIcon(R.drawable.ic_baseline_chevron_left_24)
        newNoteToolBar.setNavigationOnClickListener {
            returnNote()
        }


        if(intent.getBooleanExtra("isViewOrUpdate", false)){
            noteAlreadyAvailable = intent.getSerializableExtra("note") as Note
            setViewOrUpdateNote()
        }else{
            // default color be white
            selectedNoteColor = "#FFFFFFFF"
            //setting the date
            dateIndian = SimpleDateFormat("dd MMMM yyyy HH:mm a", Locale.getDefault())
                .format(Date())
            date.setText(
                dateIndian
            )
        }


        colorIndicator()
        initMiscellaneous()
        setSubtitleIndicatorColor()
    }

    // menu is inflated
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater : MenuInflater = menuInflater
        inflater.inflate(R.menu.top_app_bar_new_note, menu)
        return true
    }

    // if selected
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.submitNote -> {
                returnNote()
            }
        }
        return super.onOptionsItemSelected(item)
    }

    // resending the note
    private fun returnNote(){
        val returnIntent = Intent()

        if(noteTitle.text == null || noteTitle.text?.isEmpty() == true ){
            setResult(RESULT_CANCELED, returnIntent)
        }else{
            returnIntent.putExtra(NOTE_ADDED, noteTitle.text.toString())
            returnIntent.putExtra(NOTE_SUBTITLE, noteSubTitle.text.toString())
            returnIntent.putExtra(NOTE_DESCRIPTION, noteDescription.text.toString())
            returnIntent.putExtra(NOTE_COLOR, selectedNoteColor)
            if(noteAlreadyAvailable != null){
                returnIntent.putExtra(NOTE_ID, noteAlreadyAvailable!!.id)
            }
            returnIntent.putExtra(NOTE_IMPORTANT, importantval)
            returnIntent.putExtra(NOTE_COMPLETE, complete)
            returnIntent.putExtra(NOTE_DATE, dateIndian)
            returnIntent.putExtra(RESULT_DELETE, 0)
            setResult(RESULT_OK, returnIntent)
        }

        finish()
    }

    // initializing the colors
    private fun initMiscellaneous(){
        val bottomSheetBehavior = BottomSheetBehavior.from(layoutMiscellaneous)

        textMiscellaneous.setOnClickListener {
            if(bottomSheetBehavior.state != BottomSheetBehavior.STATE_EXPANDED){
                bottomSheetBehavior.state = BottomSheetBehavior.STATE_EXPANDED
            }else{
                bottomSheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED
            }
        }

        viewColor1.setOnClickListener(this)
        viewColor2.setOnClickListener(this)
        viewColor3.setOnClickListener(this)
        viewColor4.setOnClickListener(this)
        viewColor5.setOnClickListener(this)

        Complete.setOnClickListener(this)
        Important.setOnClickListener(this)
        Delete.setOnClickListener(this)

        if(noteAlreadyAvailable != null){
            Delete.visibility = View.VISIBLE
        }
    }

    private fun setSubtitleIndicatorColor(){
        val gradientDrawable : ColorDrawable = viewSubtitleIndicator.background as ColorDrawable
        gradientDrawable.setColor(Color.parseColor(selectedNoteColor))
    }

    override fun onClick(p0: View?) {
        when(p0?.id){
            R.id.viewColor1 -> {
                selectedNoteColor = "#ffcc80"
                imageColor1.setImageResource(R.drawable.ic_baseline_done_24)
                imageColor2.setImageResource(0)
                imageColor3.setImageResource(0)
                imageColor4.setImageResource(0)
                imageColor5.setImageResource(0)
            }
            R.id.viewColor2 -> {
                selectedNoteColor = "#FFFFFFFF"
                imageColor1.setImageResource(0)
                imageColor2.setImageResource(R.drawable.ic_baseline_done_24)
                imageColor3.setImageResource(0)
                imageColor4.setImageResource(0)
                imageColor5.setImageResource(0)
                setSubtitleIndicatorColor()
            }
            R.id.viewColor3 -> {
                selectedNoteColor = "#AEFBA0"
                imageColor1.setImageResource(0)
                imageColor2.setImageResource(0)
                imageColor3.setImageResource(R.drawable.ic_baseline_done_24)
                imageColor4.setImageResource(0)
                imageColor5.setImageResource(0)
            }
            R.id.viewColor4 -> {
                selectedNoteColor = "#7ec8e3"
                imageColor1.setImageResource(0)
                imageColor2.setImageResource(0)
                imageColor3.setImageResource(0)
                imageColor4.setImageResource(R.drawable.ic_baseline_done_24)
                imageColor5.setImageResource(0)
            }
            R.id.viewColor5 -> {
                selectedNoteColor = "#ff6666"
                imageColor1.setImageResource(0)
                imageColor2.setImageResource(0)
                imageColor3.setImageResource(0)
                imageColor4.setImageResource(0)
                imageColor5.setImageResource(R.drawable.ic_baseline_done_24)
            }
            R.id.Important -> {
                importantval = !importantval
                if (importantval) {
                    Toast.makeText(this, "Marked important", Toast.LENGTH_SHORT).show()

                    val returnIntent = Intent()
                    returnIntent.putExtra(NOTE_ADDED, noteTitle.text.toString())
                    returnIntent.putExtra(NOTE_SUBTITLE, noteSubTitle.text.toString())
                    returnIntent.putExtra(NOTE_DESCRIPTION, noteDescription.text.toString())
                    returnIntent.putExtra(NOTE_COLOR, selectedNoteColor)
                    if(noteAlreadyAvailable != null){
                        returnIntent.putExtra(NOTE_ID, noteAlreadyAvailable!!.id)
                    }
                    returnIntent.putExtra(NOTE_IMPORTANT, importantval)
                    returnIntent.putExtra(NOTE_COMPLETE, complete)
                    returnIntent.putExtra(NOTE_DATE, dateIndian)
                    returnIntent.putExtra(RESULT_DELETE, 4)
                    setResult(RESULT_OK, returnIntent)
                    finish()
                }
                else {
                    Toast.makeText(this, "Marked un-important", Toast.LENGTH_SHORT).show()

                    val returnIntent = Intent()
                    returnIntent.putExtra(NOTE_ADDED, noteTitle.text.toString())
                    returnIntent.putExtra(NOTE_SUBTITLE, noteSubTitle.text.toString())
                    returnIntent.putExtra(NOTE_DESCRIPTION, noteDescription.text.toString())
                    returnIntent.putExtra(NOTE_COLOR, selectedNoteColor)
                    if(noteAlreadyAvailable != null){
                        returnIntent.putExtra(NOTE_ID, noteAlreadyAvailable!!.id)
                    }
                    returnIntent.putExtra(NOTE_IMPORTANT, importantval)
                    returnIntent.putExtra(NOTE_COMPLETE, complete)
                    returnIntent.putExtra(NOTE_DATE, dateIndian)
                    returnIntent.putExtra(RESULT_DELETE, 5)
                    setResult(RESULT_OK, returnIntent)
                    finish()
                }
            }
            R.id.Complete -> {
                complete = !complete
                if (complete) {

                    val returnIntent = Intent()

                    returnIntent.putExtra(NOTE_ADDED, noteTitle.text.toString())
                    returnIntent.putExtra(NOTE_SUBTITLE, noteSubTitle.text.toString())
                    returnIntent.putExtra(NOTE_DESCRIPTION, noteDescription.text.toString())
                    returnIntent.putExtra(NOTE_COLOR, selectedNoteColor)
                    if(noteAlreadyAvailable != null){
                        returnIntent.putExtra(NOTE_ID, noteAlreadyAvailable!!.id)
                    }
                    returnIntent.putExtra(NOTE_IMPORTANT, importantval)
                    returnIntent.putExtra(NOTE_COMPLETE, complete)
                    returnIntent.putExtra(NOTE_DATE, dateIndian)
                    returnIntent.putExtra(RESULT_DELETE, 2)
                    setResult(RESULT_OK, returnIntent)

                    finish()
                }
                else{
                    Toast.makeText(this, "Marked in-complete", Toast.LENGTH_SHORT).show()
                    val returnIntent = Intent()

                    returnIntent.putExtra(NOTE_ADDED, noteTitle.text.toString())
                    returnIntent.putExtra(NOTE_SUBTITLE, noteSubTitle.text.toString())
                    returnIntent.putExtra(NOTE_DESCRIPTION, noteDescription.text.toString())
                    returnIntent.putExtra(NOTE_COLOR, selectedNoteColor)
                    if(noteAlreadyAvailable != null){
                        returnIntent.putExtra(NOTE_ID, noteAlreadyAvailable!!.id)
                    }
                    returnIntent.putExtra(NOTE_IMPORTANT, importantval)
                    returnIntent.putExtra(NOTE_COMPLETE, complete)
                    returnIntent.putExtra(NOTE_DATE, dateIndian)
                    returnIntent.putExtra(RESULT_DELETE, 3)
                    setResult(RESULT_OK, returnIntent)

                    finish()
                }
            }
            R.id.Delete -> {
                val returnIntent = Intent()

                returnIntent.putExtra(NOTE_ADDED, noteTitle.text.toString())
                returnIntent.putExtra(NOTE_SUBTITLE, noteSubTitle.text.toString())
                returnIntent.putExtra(NOTE_DESCRIPTION, noteDescription.text.toString())
                returnIntent.putExtra(NOTE_COLOR, selectedNoteColor)
                if(noteAlreadyAvailable != null){
                    returnIntent.putExtra(NOTE_ID, noteAlreadyAvailable!!.id)
                }
                returnIntent.putExtra(NOTE_IMPORTANT, importantval)
                returnIntent.putExtra(NOTE_COMPLETE, complete)
                returnIntent.putExtra(NOTE_DATE, dateIndian)
                returnIntent.putExtra(RESULT_DELETE, 1)
                setResult(RESULT_OK, returnIntent)

                finish()
            }
        }

        colorIndicator()
        setSubtitleIndicatorColor()
    }

    // updating the note
    private fun setViewOrUpdateNote(){
        noteTitle.setText(noteAlreadyAvailable!!.title)
        selectedNoteColor = noteAlreadyAvailable!!.colorNoteSpecific
        importantval = noteAlreadyAvailable!!.isImportant
        complete = noteAlreadyAvailable!!.isComplete
        dateIndian = noteAlreadyAvailable!!.date
        date.setText(dateIndian)
        noteSubTitle.setText(noteAlreadyAvailable!!.subTitle)
        noteDescription.setText(noteAlreadyAvailable!!.description)

        when(selectedNoteColor){
            "#ffcc80" -> {
                imageColor1.setImageResource(R.drawable.ic_baseline_done_24)
                imageColor2.setImageResource(0)
                imageColor3.setImageResource(0)
                imageColor4.setImageResource(0)
                imageColor5.setImageResource(0)
                setSubtitleIndicatorColor()
            }
            "#FFFFFFFF" -> {
                imageColor1.setImageResource(0)
                imageColor2.setImageResource(R.drawable.ic_baseline_done_24)
                imageColor3.setImageResource(0)
                imageColor4.setImageResource(0)
                imageColor5.setImageResource(0)
                setSubtitleIndicatorColor()
            }
            "#AEFBA0" -> {
                imageColor1.setImageResource(0)
                imageColor2.setImageResource(0)
                imageColor3.setImageResource(R.drawable.ic_baseline_done_24)
                imageColor4.setImageResource(0)
                imageColor5.setImageResource(0)
                setSubtitleIndicatorColor()
            }
            "#7ec8e3" -> {
                imageColor1.setImageResource(0)
                imageColor2.setImageResource(0)
                imageColor3.setImageResource(0)
                imageColor4.setImageResource(R.drawable.ic_baseline_done_24)
                imageColor5.setImageResource(0)
                setSubtitleIndicatorColor()
            }
            "#ff6666"-> {
                imageColor1.setImageResource(0)
                imageColor2.setImageResource(0)
                imageColor3.setImageResource(0)
                imageColor4.setImageResource(0)
                imageColor5.setImageResource(R.drawable.ic_baseline_done_24)
                setSubtitleIndicatorColor()
            }
        }
        colorIndicator()
    }

    private fun colorIndicator(){
        if(selectedNoteColor == "#FFFFFFFF"){
            viewSubtitleIndicator.visibility = View.GONE
        }else{
            viewSubtitleIndicator.visibility = View.VISIBLE
        }
    }
}