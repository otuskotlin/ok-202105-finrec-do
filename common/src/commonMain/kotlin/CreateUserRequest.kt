expect class CreateUserRequest() {
    fun request(user: UserDto): String
    suspend fun suspendRequest(user: UserDto): String
    val platform: String
}