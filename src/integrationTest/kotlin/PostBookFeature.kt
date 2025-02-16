import io.demo.mslibrary.infrastructure.Application
import io.restassured.module.mockmvc.RestAssuredMockMvc.given
import io.restassured.module.mockmvc.RestAssuredMockMvc.mockMvc
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.web.servlet.MockMvc

@SpringBootTest(classes = [Application::class])
@AutoConfigureMockMvc
class PostBookFeature {
    @Autowired
    private lateinit var mvc: MockMvc

    @BeforeEach
    fun setUp() {
        mockMvc(mvc)
    }

    @Test
    fun `should post a book`() {
        given()
            .contentType("application/json")
            .body("{\"title\": \"The Stand\"}")
            .post("/books")
            .then()
            .statusCode(201)
    }
}