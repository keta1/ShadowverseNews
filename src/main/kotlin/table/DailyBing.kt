package table

import org.jetbrains.exposed.dao.LongEntity
import org.jetbrains.exposed.dao.LongEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.LongIdTable
import org.jetbrains.exposed.sql.kotlin.datetime.timestamp

object BingTable : LongIdTable(name = "daily_bing") {
    val title = text("title")
    val pubDate = timestamp("pubdate")
    val firstFrame = text("first_frame")
    val bvId = varchar("bvid", 12)
    val desc = text("desc")
}

class BingDao(id: EntityID<Long>) : LongEntity(id) {
    companion object : LongEntityClass<BingDao>(BingTable) {
        private val bing = BingTable
        fun findByBvId(bvId: String): BingDao? = find { bing.bvId eq bvId }.firstOrNull()
    }

    var title by bing.title
    var pubDate by bing.pubDate
    var firstFrame by bing.firstFrame
    var bvId by bing.bvId
    var desc by bing.desc
}
