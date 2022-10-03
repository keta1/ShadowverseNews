package funtion

import Constants
import botSelf
import dev.inmo.tgbotapi.extensions.api.chat.get.getChat
import dev.inmo.tgbotapi.extensions.api.send.media.sendPhoto
import dev.inmo.tgbotapi.extensions.behaviour_builder.BehaviourContext
import dev.inmo.tgbotapi.extensions.behaviour_builder.triggers_handling.onCommandWithArgs
import dev.inmo.tgbotapi.extensions.utils.asUser
import dev.inmo.tgbotapi.extensions.utils.extensions.raw.from
import dev.inmo.tgbotapi.extensions.utils.formatting.codeMarkdown
import dev.inmo.tgbotapi.extensions.utils.formatting.linkMarkdown
import dev.inmo.tgbotapi.requests.abstracts.InputFile
import dev.inmo.tgbotapi.types.ChatId
import dev.inmo.tgbotapi.types.ChatIdentifier
import dev.inmo.tgbotapi.types.Username
import dev.inmo.tgbotapi.types.message.Markdown
import dev.inmo.tgbotapi.utils.PreviewFeature
import dev.inmo.tgbotapi.utils.RiskFeature
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.onFailure
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.datetime.Instant
import logger
import org.jetbrains.exposed.sql.SortOrder
import org.jetbrains.exposed.sql.transactions.transaction
import table.BingDao
import table.BingTable
import table.SendHistoryDao
import table.SettingsDao
import utils.BiliAPi
import utils.BotUtils.detailName
import utils.BotUtils.getGroupAdmin
import utils.BotUtils.sendMsgAutoDelIfGroup
import java.util.*
import kotlin.concurrent.timer
import kotlin.time.Duration
import kotlin.time.DurationUnit

private val channel = Channel<Long>(Channel.BUFFERED)
private val allChat = SettingsDao.all().forUpdate()
private val day = Duration.parse("24h").toLong(DurationUnit.MILLISECONDS)

context(BehaviourContext)
@OptIn(RiskFeature::class, PreviewFeature::class)
suspend fun installSendBing() {
    launch(Dispatchers.IO) {
        for (id in channel) {
            val (isSend, newest) = transaction {
                val bing = BingDao.all().orderBy(BingTable.pubDate to SortOrder.DESC).first()
                val isSend = SendHistoryDao.all()
                    .any { it.chatId == id && it.bing == bing.id.value }
                isSend to bing
            }
            if (isSend) {
                logger.info { "skip send because repeat" }
                break
            }
            logger.info { "send ${newest.title} the newest bing to $id" }
            val code = newest.desc.substringAfter("国服永久码：").substringBefore("\n")
            val tLevel = newest.desc.substringAfter("预计T").substringBefore("。")
            val msg = "国服永久码：\n${code.codeMarkdown()}\n" +
                    "详情：${newest.bvId.linkMarkdown("https://www.bilibili.com/video/" + newest.bvId)}\n" +
                    "#每日饼 #t$tLevel"
            sendPhoto(ChatId(id), InputFile.fromUrl(newest.firstFrame), text = msg, parseMode = Markdown)
            transaction {
                SendHistoryDao.new {
                    this.chatId = id
                    this.bing = newest.id.value
                }
            }
        }
    }

    launch(Dispatchers.IO) {
        val date = Date().apply {
            hours = 9
            minutes = 0
        }
        timer("Send msg", true, date, day) {
            val id = transaction {
                allChat.map { it.id.value }
            }
            id.forEach {
                channel.trySend(it)
                    .onFailure {
                        logger.info { "trySend msg to $it failed" }
                    }
            }
        }
        timer("Update Bing", false, Date(), day) {
            runBlocking {
                val videos = BiliAPi.getVideo("82389")
                if (videos.code != 0) logger.info { "call getVideo: code is ${videos.code}" }
                videos.data.list.vlist
                    .filter { "【饼之诗】" in it.title }
                    .map { BiliAPi.getVideoDetail(it.bvId) }
                    .forEach {
                        val data = it.data
                        transaction {
                            val has = BingDao.findById(data.aid)
                            if (has != null) return@transaction
                            BingDao.new(data.aid) {
                                this.title = data.title
                                this.pubDate = Instant.fromEpochSeconds(data.pubDate, 0)
                                this.firstFrame = data.pages.first().firstFrame
                                this.bvId = data.bvId
                                this.desc = data.desc
                            }
                        }
                    }
            }
        }
    }
    onCommandWithArgs("bindChannel") { msg, args ->
        val user = msg.from ?: return@onCommandWithArgs
        logger.debug("/bindTime command from ${user.detailName}, args is ${args.joinToString(",")}")
        val id: ChatIdentifier? = kotlin.runCatching {
            ChatId(args[0].toLong())
        }.getOrNull() ?: kotlin.runCatching {
            Username(args[0])
        }.getOrNull()
        val chat = kotlin.runCatching {
            getChat(id!!)
        }.getOrNull()
        if (chat == null) {
            sendMsgAutoDelIfGroup(msg.chat, Constants.argError, replyToMessageId = msg.messageId)
            return@onCommandWithArgs
        }
        getGroupAdmin(chat.id, botSelf.asUser()) ?: run {
            sendMsgAutoDelIfGroup(msg.chat, Constants.botNotAdmin.format(chat.id), replyToMessageId = msg.messageId)
            return@onCommandWithArgs
        }
        transaction {
            val setting = SettingsDao.findById(chat.id.chatId)
            if (setting != null) return@transaction
            SettingsDao.new(chat.id.chatId) {
                this.sendTime = 9
            }
        }
        sendMsgAutoDelIfGroup(msg.chat, Constants.bindSuccess)
    }
}
