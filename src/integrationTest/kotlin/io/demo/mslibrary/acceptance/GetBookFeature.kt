package io.demo.mslibrary.acceptance

import io.demo.mslibrary.infrastructure.repositories.LibraryRepositoryForTest
import io.restassured.module.mockmvc.RestAssuredMockMvc.given
import java.net.HttpURLConnection.HTTP_OK
import java.util.UUID
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.test.web.client.match.MockRestRequestMatchers.content

class GetBookFeature : IntegrationTestBase() {
    @Autowired lateinit var libraryRepositoryForTest: LibraryRepositoryForTest

    @Test
    fun `should get a book`() {
        val bookId = UUID.randomUUID()
        val title = "It"
        val author = "Stephen King"
        val category = "HORROR"
        val publishedYear = 1986
        val isbn = "978-0-670-81302-5"

        libraryRepositoryForTest.createBook(bookId, title, author, category, publishedYear, isbn)

        given().get("/books/{id}", bookId).then().statusCode(HTTP_OK).contentType("application/json").assertThat {
            content()
                .json(
                    """
                {
                    "id": "$bookId",
                    "title": "$title",
                    "author": "$author",
                    "category": "$category",
                    "publishedYear": $publishedYear,
                    "isbn": "$isbn"
                }
                """)
        }
    }
}
