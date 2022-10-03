import kotlinx.coroutines.runBlocking
import utils.BiliAPi
import kotlin.test.Test

class ParseVideo {
    @Test
    fun videoList() {
        runBlocking {
            val videos = BiliAPi.getVideo("82389")
            assert(videos.code == 0) { "-400：请求错误\n-412：请求被拦截" }
            videos.data.list.vlist
                .filter { "【饼之诗】" in it.title }
                .map { BiliAPi.getVideoDetail(it.bvId) }
                .forEach {
                    assert(it.code == 0) {
                        "-400：请求错误\n-403：权限不足\n-404：无视频\n62002：稿件不可见\n62004：稿件审核中"
                    }
                    val data = it.data
                    println("${data.title}#${data.bvId}#${data.pages.first().firstFrame}")
                }
        }
    }
}
