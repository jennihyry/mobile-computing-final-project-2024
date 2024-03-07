package com.example.mobilecomputingfinalproject

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert

@Dao
interface MessageDao {
    // Functions to modify our table
    @Upsert
    suspend fun upsertMessage(message: Message)

    @Query("SELECT * FROM Message ORDER BY id ASC")
    suspend fun getAllMessages(): List<Message>
}