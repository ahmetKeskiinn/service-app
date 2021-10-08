package example.com.serviceapp.di

import dagger.Module
import dagger.Provides
import example.com.serviceapp.utils.services.BackgroundService
import example.com.serviceapp.utils.services.ForegroundService

@Module
class AndroidServiceModule(
    val foregroundService: ForegroundService,
    val backgroundService: BackgroundService
) {

    @Provides
    fun provideForegroundService(): ForegroundService {
        return foregroundService
    }
    @Provides
    fun provideBackgroundService(): BackgroundService {
        return backgroundService
    }
}
