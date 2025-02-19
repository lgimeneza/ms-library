package io.demo.mslibrary.acceptance

import io.demo.mslibrary.infrastructure.Application
import io.demo.mslibrary.infrastructure.helper.ContainersInitializer
import io.demo.mslibrary.infrastructure.repositories.LibraryRepositoryForTest
import io.restassured.module.mockmvc.RestAssuredMockMvc.given
import io.restassured.module.mockmvc.RestAssuredMockMvc.mockMvc
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.web.servlet.MockMvc

@SpringBootTest(classes = [Application::class])
@AutoConfigureMockMvc
@ContextConfiguration(initializers = [ContainersInitializer::class])
class PostBookFeature {
    @Autowired private lateinit var mvc: MockMvc
    @Autowired lateinit var libraryRepositoryForTest: LibraryRepositoryForTest

    @BeforeEach
    fun setUp() {
        mockMvc(mvc)
    }

    @Test
    fun `should post a book`() {
        val bookTitle = "The Stand"
        val requestBody =
            """
        {
            "title": "$bookTitle"
        }
        """
        given().contentType("application/json").body(requestBody).post("/books").then().statusCode(201)

        assertTrue(libraryRepositoryForTest.exists(bookTitle))
    }
}
