package gaur.himanshu.imagesearchapp.data.local

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import gaur.himanshu.imagesearchapp.data.model.local.ImageEntity

@Dao
interface ImageDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(list: List<ImageEntity>)

    @Query("SELECT * FROM IMAGEENTITY WHERE `query`=:q")
    fun getImages(q: String): PagingSource<Int, ImageEntity>


    @Query("DELETE FROM IMAGEENTITY WHERE `query`=:q")
    suspend fun nukeTable(q: String)

    @Query("SELECT COUNT(*) FROM IMAGEENTITY WHERE `query`=:q")
    suspend fun getCountCorrespondingToQuery(q: String): Int

}