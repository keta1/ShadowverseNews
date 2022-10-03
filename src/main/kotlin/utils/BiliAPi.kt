package utils

import commonHttpClient
import data.Video
import data.VideoList
import io.ktor.client.request.*
import io.ktor.client.statement.*

object BiliAPi {
    suspend fun getVideo(
        id: String,
        ps: String = "7",
        pn: String = "1"
    ): VideoList {
        return commonHttpClient.get("https://api.bilibili.com/x/space/arc/search") {
            url {
                parameters["mid"] = id
                parameters["ps"] = ps
                parameters["pn"] = pn
            }
        }.bodyAsText().decodeToDataClass()
    }

    suspend fun getVideoDetail(bvId: String): Video {
        return commonHttpClient.get("https://api.bilibili.com/x/web-interface/view") {
            url {
                parameters["bvid"] = bvId
            }
        }.bodyAsText().decodeToDataClass()
    }
}
