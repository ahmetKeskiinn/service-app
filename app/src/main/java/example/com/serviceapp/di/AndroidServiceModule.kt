package example.com.serviceapp.di

import dagger.Module
import dagger.Provides
import example.com.serviceapp.utils.services.ForegroundService

@Module
class AndroidServiceModule(val mService: ForegroundService) {

    @Provides
    fun provideMyService(): ForegroundService {
        return mService
    }
}
