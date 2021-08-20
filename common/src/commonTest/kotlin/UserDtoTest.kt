import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class UserDtoTest {

    @Test
    fun createUserTest() {
        assertEquals("Dima", UserDto("Dima").name)
    }

    @Test
    fun requestUserTest() {
        val result = CreateUserRequest().request(UserDto("Dima"))
        assertTrue("Create user request mast return user name and information \"from\"") {
            result.contains("Dima")
            result.contains("from")
        }
    }
}