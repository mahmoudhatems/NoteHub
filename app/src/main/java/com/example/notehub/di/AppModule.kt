package com.example.notehub.di

import android.content.Context
import androidx.room.Room
import com.example.notehub.data.db.NoteDao
import com.example.notehub.data.db.NotesDatabase
import com.example.notehub.data.repository.NotesRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext appContext: Context): NotesDatabase {
        return Room.databaseBuilder(
            appContext,
            NotesDatabase::class.java,
            "notes_db"
        ).build()
    }
    @Provides
    fun provideNotesDao(database: NotesDatabase): NoteDao {
        return database.notesDao() ?: throw IllegalStateException("NotesDao cannot be null")
    }


    @Provides
    @Singleton
    fun provideNotesRepository(notesDao: NoteDao): NotesRepository {
        return NotesRepository(notesDao)
    }
}