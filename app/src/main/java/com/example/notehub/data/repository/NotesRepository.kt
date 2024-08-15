package com.example.notehub.data.repository

import com.example.notehub.data.db.Note
import com.example.notehub.data.db.NoteDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class NotesRepository @Inject constructor(
    private val notesDao: NoteDao
) {
    suspend fun getAllNotes() = notesDao.getAll()

    suspend fun insert(note: Note) {
        notesDao.insert(note)
    }

    suspend fun update(note: Note) {
        notesDao.update(note)
    }

    suspend fun delete() {
        notesDao.deleteAll()
    }
}
