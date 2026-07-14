package com.example.data

import android.content.Context
import androidx.room.Room

object DataModule {
    private var database: AppDatabase? = null

    fun getDatabase(context: Context): AppDatabase {
        return database ?: Room.databaseBuilder(
            context.applicationContext,
            AppDatabase::class.java,
            "medtest_database"
        ).build().also { database = it }
    }
}
