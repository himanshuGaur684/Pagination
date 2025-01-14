package gaur.himanshu.imagesearchapp

import android.content.Context
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
object AppModule {

    @Provides
    @Singleton
    fun provideAppDatabase(@ApplicationContext context: Context) = AppDatabase.getInstance(context)

    @Provides
    @Singleton
    fun provideImageDao(db: AppDatabase) = db.getImageDao()


    @Provides
    @Singleton
    fun provideRemoteKeysDao(db: AppDatabase) = db.getRemoteKeyDao()
}