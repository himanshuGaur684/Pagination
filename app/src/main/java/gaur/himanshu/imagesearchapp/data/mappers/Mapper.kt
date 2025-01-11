package gaur.himanshu.imagesearchapp.data.mappers

interface Mapper<F, T> {
    fun map(from: F): T
}

fun <F, T> Mapper<F, T>.mapAll(list: List<F>): List<T> = list.map { map(from = it) }