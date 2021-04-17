package haylahay.testdrivendev

import android.app.Application
import androidx.room.Room
import haylahay.testdrivendev.data.AppDatabase

class TDDApplication : Application() {

    val db by lazy {
        Room.databaseBuilder(
            applicationContext,
            AppDatabase::class.java, "database-name"
        ).build()
    }
}