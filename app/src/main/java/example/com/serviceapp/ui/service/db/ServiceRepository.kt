package example.com.serviceapp.ui.service.db

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ServiceRepository(db: ServiceDataBase) {

    private var favDao: DAO = db.getFavDao()

    fun insertMovie(model: ServiceModel) {
        CoroutineScope(Dispatchers.IO).launch {
            favDao.addMovie(model)
        }
    }
}
