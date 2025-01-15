package gaur.himanshu.imagesearchapp.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import gaur.himanshu.imagesearchapp.data.model.local.RemoteKey

@Dao
interface RemoteKeyDao {


    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(list: List<RemoteKey>)

    @Query("SELECT * FROM REMOTEKEY WHERE `id`=:id")
    suspend fun getRemoteKey(id: String): RemoteKey?

    @Query("DELETE FROM REMOTEKEY WHERE `query`=:q")
    suspend fun deleteRemoteKeyQuery(q: String)

}