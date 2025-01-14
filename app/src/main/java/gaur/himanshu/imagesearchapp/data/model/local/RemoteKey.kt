package gaur.himanshu.imagesearchapp.data.model.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class RemoteKey(
    @PrimaryKey(autoGenerate = false)
    val imageId: String,
    val prevKey: Int?,
    val nextKey: Int?,
    val query: String,
)
