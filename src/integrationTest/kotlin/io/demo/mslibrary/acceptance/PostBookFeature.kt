package io.demo.mslibrary.acceptance

import io.demo.mslibrary.infrastructure.repositories.LibraryRepositoryForTest
import io.restassured.module.mockmvc.RestAssuredMockMvc.given
import java.net.HttpURLConnection.HTTP_CREATED
import java.util.UUID
import org.assertj.core.api.Assertions.assertThat
import org.awaitility.Awaitility.await
import org.awaitility.Durations.TEN_SECONDS
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired

class PostBookFeature : IntegrationTestBase() {

    @Autowired lateinit var libraryRepositoryForTest: LibraryRepositoryForTest

    @Test
    fun `should post a book`() {
        kafkaConsumerForTest.consumeFrom("pub.library.books")
        val bookTitle = "The Stand"
        val author = "Stephen King"
        val category = "FANTASY"
        val publishedYear = 1978
        val isbn = "978-0-385-12168-" + (1000..9999).random()
        val requestBody =
            """
        {
            "title": "$bookTitle",
            "author": "$author",
            "category": "$category",
            "publishedYear": "$publishedYear",
            "isbn": "$isbn"
        }
        """
        given().contentType("application/json").body(requestBody).post("/books").then().statusCode(HTTP_CREATED)

        assertTrue(libraryRepositoryForTest.exists(bookTitle, author, category, publishedYear, isbn))

        await().atMost(TEN_SECONDS).untilAsserted {
            val events = kafkaConsumerForTest.consumeAtLeastOne()
            assertThat(events).anyMatch {
                eventsAreEqual(
                    it.value(),
                    """
                    {
                        "id": "${UUID.randomUUID()}",
                        "type": "BookRegistered",
                        "schema": "https://demo.io/schemas/library/book-registered.json",
                        "version": "1.0",
                        "published": "${it.timestamp()}",
                        "book": {
                            "title": "$bookTitle",
                            "author": "$author",
                            "category": "$category",
                            "publishedYear": $publishedYear,
                            "isbn": "$isbn"
                        }
                    }
                    """,
                    pathsToBeIgnored = arrayOf("id", "published", "schema"))
            }
        }
    }
}
