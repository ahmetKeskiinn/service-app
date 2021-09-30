package example.com.serviceapp.ui.service.db

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy

@Dao
interface DAO {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun addMovie(model: ServiceModel)

    @Delete
    suspend fun deleteMovie(model: ServiceModel?)
}
