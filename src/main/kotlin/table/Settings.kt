package table

import org.jetbrains.exposed.dao.LongEntity
import org.jetbrains.exposed.dao.LongEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.LongIdTable

object SettingsTable : LongIdTable(name = "settings") {
    // 每日发送时间
    val sendTime = integer("send_time")
}

class SettingsDao(id: EntityID<Long>) : LongEntity(id) {
    private val bing = SettingsTable
    companion object : LongEntityClass<SettingsDao>(SettingsTable)

    var sendTime by bing.sendTime
}