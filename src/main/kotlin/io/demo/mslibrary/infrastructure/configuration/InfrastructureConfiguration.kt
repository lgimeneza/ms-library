package io.demo.mslibrary.infrastructure.configuration

import io.demo.mslibrary.domain.BooksRepository
import io.demo.mslibrary.infrastructure.repositories.InMemoryBooksRepository
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class InfrastructureConfiguration {
    @Bean fun booksRepository(): BooksRepository = InMemoryBooksRepository()
}
