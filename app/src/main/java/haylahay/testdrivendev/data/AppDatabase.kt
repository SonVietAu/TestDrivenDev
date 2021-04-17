package haylahay.testdrivendev.data

import androidx.room.Database
import androidx.room.RoomDatabase



@Database(
        entities = arrayOf(
                Company::class
        ), version = 1
)
abstract class AppDatabase : RoomDatabase() {
        abstract fun getCompaniesDao(): CompaniesDao
}