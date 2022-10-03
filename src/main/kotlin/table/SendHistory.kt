package table

import org.jetbrains.exposed.dao.LongEntity
import org.jetbrains.exposed.dao.LongEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.LongIdTable

object SendHistoryTable : LongIdTable(name = "send_history") {
    val chatId = long("chat_id").references(SettingsTable.id)
    val bing = long("bing").references(BingTable.id)
}

class SendHistoryDao(id: EntityID<Long>) : LongEntity(id) {
    companion object : LongEntityClass<SendHistoryDao>(SendHistoryTable)

    var chatId by SendHistoryTable.chatId
    var bing by SendHistoryTable.bing
}
