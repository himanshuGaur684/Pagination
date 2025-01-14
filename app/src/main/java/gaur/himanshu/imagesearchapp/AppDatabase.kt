package gaur.himanshu.imagesearchapp

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import gaur.himanshu.imagesearchapp.data.local.ImageDao
import gaur.himanshu.imagesearchapp.data.local.RemoteKeysDao
import gaur.himanshu.imagesearchapp.data.model.local.ImageEntity
import gaur.himanshu.imagesearchapp.data.model.local.RemoteKey

@Database(
    entities = [ImageEntity::class, RemoteKey::class],
    version = 1,
    exportSchema = true
)
abstract class AppDatabase : RoomDatabase() {

    companion object {
        fun getInstance(context: Context) =
            Room.databaseBuilder(context, AppDatabase::class.java, "app_db")
                .build()
    }

    abstract fun getImageDao(): ImageDao
    abstract fun getRemoteKeyDao(): RemoteKeysDao

}