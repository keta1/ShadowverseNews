package utils

import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.Slf4jSqlDebugLogger
import org.jetbrains.exposed.sql.addLogger
import org.jetbrains.exposed.sql.transactions.transaction
import table.BingTable
import table.SendHistoryTable
import table.SettingsTable

object DBUtil {
    private lateinit var db: Database

    fun prepare(database: String) {
        db = Database.connect(database)
        transaction {
            addLogger(Slf4jSqlDebugLogger)

            SchemaUtils.createMissingTablesAndColumns(
                BingTable,
                SettingsTable,
                SendHistoryTable
            )
        }
    }
}
