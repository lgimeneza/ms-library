package io.demo.mslibrary.acceptance

import io.demo.mslibrary.infrastructure.repositories.LibraryRepositoryForTest
import io.restassured.module.mockmvc.RestAssuredMockMvc.given
import java.net.HttpURLConnection.HTTP_NOT_FOUND
import java.net.HttpURLConnection.HTTP_NO_CONTENT
import java.util.UUID
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired

class DeleteBookFeature : IntegrationTestBase() {
    @Autowired lateinit var libraryRepositoryForTest: LibraryRepositoryForTest

    @Test
    fun `should delete a book`() {
        val bookId = UUID.randomUUID()
        val title = "It"
        val author = "Stephen King"
        val category = "HORROR"
        val publishedYear = 1986
        val isbn = "978-0-670-81302-" + (1000..9999).random()
        libraryRepositoryForTest.createBook(bookId, title, author, category, publishedYear, isbn)

        given().delete("/books/{id}", bookId).then().statusCode(HTTP_NO_CONTENT)

        assertFalse(libraryRepositoryForTest.existsById(bookId))
    }

    @Test
    fun `should return 404 when book does not exist`() {
        val nonExistentBookId = UUID.randomUUID()

        given().delete("/books/{id}", nonExistentBookId).then().statusCode(HTTP_NOT_FOUND)
    }
}
