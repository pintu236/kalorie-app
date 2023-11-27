package com.indieme.kalorie

import android.app.Application
import android.content.Context
import androidx.room.Room

class KalorieApplication : Application() {
    val database: AppDatabase by lazy {
        Room.databaseBuilder(
            this,
            AppDatabase::class.java,
            "kalorie_database"
        ).fallbackToDestructiveMigration().build()
    }

    override fun onCreate() {
        super.onCreate()
        mApplicationContext = applicationContext;
    }


    companion object {
        @JvmField
        var mApplicationContext: Context? = null
    }

}