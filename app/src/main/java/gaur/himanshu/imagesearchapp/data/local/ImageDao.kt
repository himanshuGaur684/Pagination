package gaur.himanshu.imagesearchapp.data.local

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import gaur.himanshu.imagesearchapp.data.model.local.IMAGE_TABLE
import gaur.himanshu.imagesearchapp.data.model.local.ImageEntity

@Dao
interface ImageDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(item: List<ImageEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(item: ImageEntity)

    @Query("SELECT * FROM $IMAGE_TABLE WHERE `query`=:q")
    fun getPages(q: String): PagingSource<Int, ImageEntity>

    @Query("SELECT COUNT(*) FROM $IMAGE_TABLE WHERE `query` =:q")
    suspend fun countBasedOnQuery(q: String): Int

}