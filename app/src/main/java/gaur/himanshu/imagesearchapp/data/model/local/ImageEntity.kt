package gaur.himanshu.imagesearchapp.data.model.local

import androidx.room.Entity
import androidx.room.PrimaryKey

const val IMAGE_TABLE = "image_table"

@Entity(IMAGE_TABLE)
data class ImageEntity(
    @PrimaryKey(autoGenerate = false)
    val id: String,
    val imageUrl: String,
    val query: String
)
