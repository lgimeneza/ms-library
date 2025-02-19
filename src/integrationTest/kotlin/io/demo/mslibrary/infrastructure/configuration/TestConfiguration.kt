package io.demo.mslibrary.infrastructure.configuration

import io.demo.mslibrary.infrastructure.repositories.LibraryRepositoryForTest
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate

@Configuration
class TestConfiguration {
    @Bean
    fun libraryRepositoryForTest(jdbcTemplate: NamedParameterJdbcTemplate) = LibraryRepositoryForTest(jdbcTemplate)
}
