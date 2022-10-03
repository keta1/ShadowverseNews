import dev.inmo.tgbotapi.extensions.api.bot.getMe
import dev.inmo.tgbotapi.extensions.api.send.sendMessage
import dev.inmo.tgbotapi.extensions.api.telegramBot
import dev.inmo.tgbotapi.extensions.behaviour_builder.buildBehaviourWithLongPolling
import dev.inmo.tgbotapi.extensions.behaviour_builder.triggers_handling.onCommand
import dev.inmo.tgbotapi.types.chat.ExtendedBot
import dev.inmo.tgbotapi.utils.TelegramAPIUrlsKeeper
import funtion.installSendBing
import io.ktor.client.*
import io.ktor.client.engine.okhttp.*
import io.ktor.client.plugins.*
import io.ktor.util.reflect.*
import io.ktor.utils.io.*
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import mu.KotlinLogging
import utils.DBUtil
import java.io.File

val config = Json.decodeFromString<Config>(File("data/config.json").readText())

val commonHttpClient = HttpClient(OkHttp)

val logger = KotlinLogging.logger {}
lateinit var botSelf: ExtendedBot

suspend fun main() {
    DBUtil.prepare(config.database)
    val telegramBotAPIUrlsKeeper = TelegramAPIUrlsKeeper(config.token, config.botApiUrl)
    val bot = telegramBot(telegramBotAPIUrlsKeeper, commonHttpClient)
    logger.info("Bot start")

    bot.buildBehaviourWithLongPolling(
        defaultExceptionsHandler = { e ->
            val ignore = listOf(
                CancellationException::class,
                HttpRequestTimeoutException::class
            )
            if (ignore.none { e.instanceOf(it) || e.cause?.instanceOf(it) == true }) {
                logger.error("Exception happened!", e)
            }
        }
    ) {
        botSelf = getMe()
        logger.info(botSelf.toString())

        onCommand("start") {
            sendMessage(it.chat, Constants.help)
        }

        installSendBing()
    }.join()
}
