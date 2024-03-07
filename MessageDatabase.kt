package com.example.mobilecomputingfinalproject

import androidx.room.Database
import androidx.room.RoomDatabase


@Database(
    entities = [Message::class],
    version = 1
)
abstract class MessageDatabase: RoomDatabase() {

    abstract val dao: MessageDao

}