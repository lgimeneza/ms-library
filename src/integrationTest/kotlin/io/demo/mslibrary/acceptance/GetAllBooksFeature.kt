package io.demo.mslibrary.acceptance

import io.demo.mslibrary.infrastructure.builders.GetBooksResponseBuilder
import io.demo.mslibrary.infrastructure.repositories.LibraryRepositoryForTest
import io.restassured.module.mockmvc.RestAssuredMockMvc.given
import java.net.HttpURLConnection.HTTP_OK
import java.util.UUID
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.test.web.client.match.MockRestRequestMatchers.content

class GetAllBooksFeature : IntegrationTestBase() {
    @Autowired lateinit var libraryRepositoryForTest: LibraryRepositoryForTest

    @Test
    fun `should get all books`() {
        val bookId1 = UUID.randomUUID()
        val title1 = "It"
        val author1 = "Stephen King"
        val category1 = "HORROR"
        val publishedYear1 = 1986
        val isbn1 = "978-0-670-81302-" + (1000..9999).random()
        val bookId2 = UUID.randomUUID()
        val title2 = "The Stand"
        val author2 = "Stephen King"
        val category2 = "FANTASY"
        val publishedYear2 = 1978
        val isbn2 = "978-0-385-12168-" + (1000..9999).random()
        val bookId3 = UUID.randomUUID()
        val title3 = "1984"
        val author3 = "George Orwell"
        val category3 = "SCIENCE_FICTION"
        val publishedYear3 = 1949
        val isbn3 = "978-0-452-28423-" + (1000..9999).random()
        libraryRepositoryForTest.createBook(bookId1, title1, author1, category1, publishedYear1, isbn1)
        libraryRepositoryForTest.createBook(bookId2, title2, author2, category2, publishedYear2, isbn2)
        libraryRepositoryForTest.createBook(bookId3, title3, author3, category3, publishedYear3, isbn3)
        val expectedJson =
            GetBooksResponseBuilder()
                .addBook(bookId1, title1, author1, category1, publishedYear1, isbn1)
                .addBook(bookId2, title2, author2, category2, publishedYear2, isbn2)
                .addBook(bookId3, title3, author3, category3, publishedYear3, isbn3)
                .build()

        given().get("/books").then().statusCode(HTTP_OK).contentType("application/json").assertThat {
            content().json(expectedJson)
        }
    }

    @Test
    fun `should return empty list when no books exist`() {
        given().get("/books").then().statusCode(HTTP_OK).contentType("application/json").assertThat {
            content().json("[]")
        }
    }
}
