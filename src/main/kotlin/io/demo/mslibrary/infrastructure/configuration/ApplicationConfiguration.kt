package io.demo.mslibrary.infrastructure.configuration

import io.demo.mslibrary.application.RegisterBook
import io.demo.mslibrary.domain.BooksRepository
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class ApplicationConfiguration {
    @Bean fun saveBook(booksRepository: BooksRepository) = RegisterBook(booksRepository)
}
