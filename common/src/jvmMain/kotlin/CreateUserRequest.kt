import kotlinx.coroutines.delay

actual class CreateUserRequest {
    actual fun request(user: UserDto): String = "User ${user.name} from $platform"

    actual val platform: String = "jvm"
    actual suspend fun suspendRequest(user: UserDto): String {
        delay(1000L)
        return "suspend ${request(user)}"
    }
}