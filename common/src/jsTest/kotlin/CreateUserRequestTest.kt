import kotlinx.coroutines.MainScope
import kotlinx.coroutines.promise
import kotlin.test.Test
import kotlin.test.assertTrue

class CreateUserRequestTest {

    @Test
    fun requestUserTest() {
        val result = CreateUserRequest().request(UserDto("Dima"))
        assertTrue("Create user request mast return user name and information \"from\"") {
            result.contains("Dima")
            result.contains("js")
        }
    }


    @Test
    fun requestUserTestSuspend() {
        MainScope().promise {
            val result = CreateUserRequest().suspendRequest(UserDto("Dima"))
            assertTrue("Create user request mast return user name and information \"from\"") {
                result.contains("suspend")
                result.contains("Dima")
                result.contains("js")
            }
        }
    }
}