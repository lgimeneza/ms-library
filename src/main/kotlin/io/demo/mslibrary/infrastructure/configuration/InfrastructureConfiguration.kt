package io.demo.mslibrary.infrastructure.configuration

import io.demo.mslibrary.domain.BooksRepository
import io.demo.mslibrary.infrastructure.repositories.PostgreSqlBooksRepository
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate

@Configuration
class InfrastructureConfiguration {
    @Bean
    fun booksRepository(jdbcTemplate: NamedParameterJdbcTemplate): BooksRepository =
        PostgreSqlBooksRepository(jdbcTemplate)
}
