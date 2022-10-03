import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import java.io.File
import kotlin.io.path.Path
import kotlin.io.path.writeText
import kotlin.test.Test

class WriteCongig {
    private val config = Config(
        "127.0.0.1",
        7890,
        "jdbc:sqlite:aaa.db",
        "https://api.telegram.org",
        "xxxxxxxx",
        5649,
        1234567890L
    )

    @Test
    fun write() {
        Path("data/config.json").writeText(Json.encodeToString(config))
    }

    @Test
    fun read() {
        val read = Json.decodeFromString<Config>(File("data/config.json").readText())
        assert(read == config)
    }
}
