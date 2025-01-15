package gaur.himanshu.imagesearchapp.data.di

import android.content.Context
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import gaur.himanshu.imagesearchapp.AppDatabase
import gaur.himanshu.imagesearchapp.data.local.ImageDao
import gaur.himanshu.imagesearchapp.data.local.RemoteKeyDao
import gaur.himanshu.imagesearchapp.data.mappers.ImageDTOToImageMapper
import gaur.himanshu.imagesearchapp.data.mappers.ImageEntityToImageMapper
import gaur.himanshu.imagesearchapp.data.remote.ApiService
import gaur.himanshu.imagesearchapp.data.repository.ImageRepoImpl
import gaur.himanshu.imagesearchapp.domain.repository.ImageRepository
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
object DataModule {
    // https://pixabay.com/api/?key=40308333-07c19e899666cb68334ed3a46&q=yellow+flowers&image_type=photo&pretty=true
    @Provides
    @Singleton
    fun provideRetrofit(): Retrofit {
        return Retrofit.Builder()
            .baseUrl("https://pixabay.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    @Provides
    fun provideApiService(retrofit: Retrofit): ApiService {
        return retrofit.create(ApiService::class.java)
    }

    @Provides
    fun provideImageRepository(
        apiService: ApiService,
        mapper: ImageDTOToImageMapper,
        imageDao: ImageDao,
        remoteKeyDao: RemoteKeyDao,
        imageEntityToImageMapper: ImageEntityToImageMapper
    ): ImageRepository {
        return ImageRepoImpl(
            apiService, mapper,
            imageDao = imageDao,
            remoteKeyDao = remoteKeyDao,
            imageEntityToImageMapper = imageEntityToImageMapper
        )
    }

    @Provides
    @Singleton
    fun provideAppDatabase(@ApplicationContext context: Context): AppDatabase {
        return AppDatabase.getInstance(context)
    }

    @Singleton
    @Provides
    fun provideImageDao(appDatabase: AppDatabase): ImageDao {
        return appDatabase.getImageDao()
    }

    @Singleton
    @Provides
    fun provideRemoteKeyDao(appDatabase: AppDatabase): RemoteKeyDao {
        return appDatabase.getRemoteKeyDao()
    }

}












