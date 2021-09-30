package example.com.serviceapp.ui.service.db

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(
    entities = [
        ServiceModel::class
    ],
    version = 1
)
abstract class ServiceDataBase : RoomDatabase() {
    abstract fun getFavDao(): DAO
}
