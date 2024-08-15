package com.example.notehub.data.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import androidx.room.Delete
import kotlinx.coroutines.flow.Flow

@Dao
interface NoteDao {
    @Query("SELECT * FROM note")
    suspend fun getAll(): List<Note>

    @Query("SELECT * FROM note WHERE isFavorite = 1")
    suspend fun getFavorites(): List<Note>

    @Insert
    suspend fun insert(note: Note)

    @Update
    suspend fun update(note: Note)

    @Delete
    suspend fun deleteNote(note: Note)

    @Query("DELETE FROM note")
    suspend fun deleteAll()

    @Query("SELECT * FROM note WHERE id = :id")
    suspend fun getById(id: Int): Note?

    @Query("DELETE FROM note WHERE id = :id")
    suspend fun deleteById(id: Int)


}