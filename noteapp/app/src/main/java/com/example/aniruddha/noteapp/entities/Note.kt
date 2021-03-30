package com.example.aniruddha.noteapp.entities

import androidx.annotation.NonNull
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity(tableName = "notes")
data class Note(@NonNull@PrimaryKey val id:String,
                @ColumnInfo(name = "title") val title: String,
                @ColumnInfo(name = "color") val colorNoteSpecific:String,
                @ColumnInfo(name = "important") val isImportant: Boolean,
                @ColumnInfo(name = "complete") val isComplete: Boolean,
                @ColumnInfo(name = "date") val date:String,
                @ColumnInfo(name = "subTitle") val subTitle:String,
                @ColumnInfo(name = "description") val description:String): Serializable
