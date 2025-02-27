package io.demo.mslibrary.infrastructure.configuration

import com.fasterxml.jackson.databind.ObjectMapper
import io.demo.mslibrary.domain.BooksEventsPublisher
import io.demo.mslibrary.domain.BooksRepository
import io.demo.mslibrary.infrastructure.repositories.PostgreSqlBooksRepository
import io.demo.mslibrary.infrastructure.streams.KafkaBooksEventsProducer
import org.springframework.cloud.stream.function.StreamBridge
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate

@Configuration
class InfrastructureConfiguration {
    @Bean
    fun booksRepository(jdbcTemplate: NamedParameterJdbcTemplate): BooksRepository =
        PostgreSqlBooksRepository(jdbcTemplate)

    @Bean
    fun booksEventPublisher(streamBridge: StreamBridge, objectMapper: ObjectMapper): BooksEventsPublisher =
        KafkaBooksEventsProducer(streamBridge, objectMapper)
}
