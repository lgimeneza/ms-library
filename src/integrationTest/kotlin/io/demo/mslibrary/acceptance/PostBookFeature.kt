package io.demo.mslibrary.acceptance

import io.demo.mslibrary.infrastructure.Application
import io.demo.mslibrary.infrastructure.helper.ContainersInitializer
import io.demo.mslibrary.infrastructure.helper.KafkaConsumerForTest
import io.demo.mslibrary.infrastructure.repositories.LibraryRepositoryForTest
import io.restassured.module.mockmvc.RestAssuredMockMvc.given
import io.restassured.module.mockmvc.RestAssuredMockMvc.mockMvc
import java.util.UUID
import net.javacrumbs.jsonunit.assertj.JsonAssertions.assertThatJson
import net.javacrumbs.jsonunit.core.Option
import org.assertj.core.api.Assertions.assertThat
import org.awaitility.Awaitility.await
import org.awaitility.Durations.TEN_SECONDS
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.core.env.Environment
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.web.servlet.MockMvc

@SpringBootTest(classes = [Application::class])
@AutoConfigureMockMvc
@ContextConfiguration(initializers = [ContainersInitializer::class])
class PostBookFeature {
    @Autowired private lateinit var mvc: MockMvc
    @Autowired lateinit var libraryRepositoryForTest: LibraryRepositoryForTest
    @Autowired lateinit var env: Environment
    lateinit var kafkaConsumerForTest: KafkaConsumerForTest

    @BeforeEach
    fun setUp() {
        val bootstrapServers = env.getProperty("spring.cloud.stream.kafka.binder.brokers")!!
        kafkaConsumerForTest = KafkaConsumerForTest(bootstrapServers)
        mockMvc(mvc)
    }

    @Test
    fun `should post a book`() {
        kafkaConsumerForTest.consumeFrom("pub.library.books")

        val bookTitle = "The Stand"
        val author = "Stephen King"
        val category = "FANTASY"
        val publishedYear = 1978
        val isbn = "978-0-385-12168-2"

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
        given().contentType("application/json").body(requestBody).post("/books").then().statusCode(201)

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

    fun eventsAreEqual(jsonEvent: String, expectedJsonEvent: String?, vararg pathsToBeIgnored: String?): Boolean {
        return try {
            assertThatJson(jsonEvent)
                .`when`(Option.IGNORING_EXTRA_FIELDS)
                .whenIgnoringPaths(*pathsToBeIgnored as Array<String>)
                .isEqualTo(expectedJsonEvent)
            true
        } catch (e: AssertionError) {
            println(e.message)
            false
        }
    }
}
