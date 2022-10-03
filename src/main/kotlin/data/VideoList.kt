package data

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class VideoList(
    val code: Int,
    val data: Data,
    val message: String,
    val ttl: Int
) {
    @Serializable
    data class Data(
        @SerialName("gaia_res_type")
        val gaiaResType: Int,
        @SerialName("is_risk")
        val isRisk: Boolean,
        val list: VList,
        val page: Page
    ) {
        @Serializable
        data class VList(
            @SerialName("vlist")
            val vlist: List<Vlist>
        ) {
            @Serializable
            data class Vlist(
                val aid: Int,
                val author: String,
                @SerialName("bvid")
                val bvId: String,
                val comment: Int,
                val copyright: String,
                val created: Int,
                val description: String,
                @SerialName("hide_click")
                val hideClick: Boolean,
                @SerialName("is_live_playback")
                val isLivePlayback: Int,
                @SerialName("is_pay")
                val isPay: Int,
                @SerialName("is_steins_gate")
                val isSteinsGate: Int,
                @SerialName("is_union_video")
                val isUnionVideo: Int,
                val length: String,
                val mid: Int,
                val pic: String,
                val play: Int,
                val review: Int,
                val subtitle: String,
                val title: String,
                @SerialName("typeid")
                val typeId: Int,
                @SerialName("video_review")
                val videoReview: Int
            )
        }

        @Serializable
        data class Page(
            val count: Int,
            val pn: Int,
            val ps: Int
        )
    }
}