package utils

import commonHttpClient
import data.Video
import data.VideoList
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*

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
            headers {
                userAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/89.0.4389.114 Safari/537.36")
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
