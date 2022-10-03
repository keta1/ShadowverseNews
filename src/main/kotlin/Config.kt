import kotlinx.serialization.Serializable

@Serializable
data class Config(
    val proxyHost: String,
    val proxyPort: Int,
    val database: String,
    val botApiUrl: String,
    val token: String,
    val serverPort: Int,
    val admin: Long
)
