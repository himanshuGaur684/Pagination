package gaur.himanshu.imagesearchapp.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import gaur.himanshu.imagesearchapp.data.model.local.RemoteKey

@Dao
interface RemoteKeysDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(remoteKeys: List<RemoteKey>)

    @Query("SELECT * FROM RemoteKey WHERE imageId=:id")
    suspend fun getRemoteKeys(id: String): RemoteKey?

    @Query("DELETE FROM RemoteKey")
    suspend fun nukeTable()

    @Query("DELETE FROM REMOTEKEY WHERE `query`=:q")
    suspend fun nukeByQuery(q: String)

}