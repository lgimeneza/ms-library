
import io.demo.mslibrary.infrastructure.Application
import org.junit.jupiter.api.Test
import org.springframework.boot.test.context.SpringBootTest

@SpringBootTest(classes = [Application::class])
class ApplicationTest {
    @Test
    fun contextLoads() {
    }
}