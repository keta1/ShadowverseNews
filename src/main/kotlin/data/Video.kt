package data

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Video(
    val code: Int,
    val data: Data,
    val message: String,
    val ttl: Int
) {
    @Serializable
    data class Data(
        val aid: Long,
        @SerialName("bvid")
        val bvId: String,
        val ctime: Int,
        val desc: String,
        val dimension: Dimension,
        val duration: Int,
        val pages: List<Page>,
        val pic: String,
        @SerialName("pubdate")
        val pubDate: Long,
        val state: Int,
        val tid: Int,
        val title: String,
        val videos: Int // 稿件分P总数
    ) {

        @Serializable
        data class Dimension(
            val height: Int,
            val width: Int,
            val rotate: Int
        )

        @Serializable
        data class Page(
            val cid: Int,
            val dimension: Dimension,
            val duration: Int,
            @SerialName("first_frame")
            val firstFrame: String,
            val page: Int,
            val part: String
        )
    }
}