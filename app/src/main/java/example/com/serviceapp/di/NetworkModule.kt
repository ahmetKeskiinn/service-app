package example.com.serviceapp.di

import android.app.Application
import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import dagger.Module
import dagger.Provides
import example.com.serviceapp.utils.GetService
import example.com.serviceapp.utils.sizeX
import example.com.serviceapp.utils.sizeY
import example.com.serviceapp.utils.sizeZ
import example.com.serviceapp.utils.timeOutOne
import example.com.serviceapp.utils.timeOutTwo
import okhttp3.Cache
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.File
import java.util.UUID
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
class NetworkModule(private val application: Application) {

    @Provides
    @Singleton
    internal fun provideOkHttpClient(): OkHttpClient {
        val interceptor = HttpLoggingInterceptor()
        interceptor.level = HttpLoggingInterceptor.Level.BASIC
        val cacheDir = File(application.cacheDir, UUID.randomUUID().toString())
        val cache = Cache(cacheDir, sizeX * sizeY * sizeZ)
        return OkHttpClient.Builder()
            .cache(
                cache
            )
            .connectTimeout(timeOutOne, TimeUnit.SECONDS)
            .readTimeout(timeOutTwo, TimeUnit.SECONDS)
            .writeTimeout(timeOutTwo, TimeUnit.SECONDS)
            .addInterceptor(interceptor)
            .build()
    }

    @Provides
    @Singleton
    internal fun provideRetrofitInterface(okHttpClient: OkHttpClient): Retrofit = Retrofit.Builder()
        .addConverterFactory(GsonConverterFactory.create())
        .addCallAdapterFactory(CoroutineCallAdapterFactory())
        .baseUrl("BuildConfig.BASE_URL")
        .client(okHttpClient)
        .build()

    @Provides
    @Singleton
    internal fun provideMovieApi(retrofit: Retrofit): GetService = retrofit.create(GetService::class.java)
}
