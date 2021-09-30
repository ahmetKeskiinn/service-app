package example.com.serviceapp.di

import android.app.Application
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import dagger.Module
import dagger.Provides
import example.com.serviceapp.ui.service.db.ServiceDataBase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Singleton

@Module
class RoomModule(val application: Application) {
    private val databaseCallback = object : RoomDatabase.Callback() {
        override fun onCreate(db: SupportSQLiteDatabase) {
            super.onCreate(db)
            CoroutineScope(Dispatchers.IO).launch {
                addSampleBooksToDatabase()
            }
        }
    }

    private fun addSampleBooksToDatabase() {
    } // burayı fragmente al oncreate view'da çağır

    @Singleton
    @Provides
    fun providesRoomDatabase(): ServiceDataBase {
        val db = Room.databaseBuilder(application, ServiceDataBase::class.java, "fav_db")
            .fallbackToDestructiveMigration()
            .addCallback(databaseCallback)
            .build()
        return db
    }
}
