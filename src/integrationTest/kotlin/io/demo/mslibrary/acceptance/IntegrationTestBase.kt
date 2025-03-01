package io.demo.mslibrary.acceptance

import io.demo.mslibrary.infrastructure.Application
import io.demo.mslibrary.infrastructure.helper.ContainersInitializer
import io.demo.mslibrary.infrastructure.helper.KafkaConsumerForTest
import io.restassured.module.mockmvc.RestAssuredMockMvc.mockMvc
import net.javacrumbs.jsonunit.assertj.JsonAssertions.assertThatJson
import net.javacrumbs.jsonunit.core.Option
import org.junit.jupiter.api.BeforeEach
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.core.env.Environment
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.web.servlet.MockMvc

@SpringBootTest(classes = [Application::class])
@AutoConfigureMockMvc
@ContextConfiguration(initializers = [ContainersInitializer::class])
class IntegrationTestBase {
    @Autowired private lateinit var mvc: MockMvc
    @Autowired lateinit var env: Environment
    lateinit var kafkaConsumerForTest: KafkaConsumerForTest

    @BeforeEach
    fun setUp() {
        val bootstrapServers = env.getProperty("spring.cloud.stream.kafka.binder.brokers")!!
        kafkaConsumerForTest = KafkaConsumerForTest(bootstrapServers)
        mockMvc(mvc)
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
