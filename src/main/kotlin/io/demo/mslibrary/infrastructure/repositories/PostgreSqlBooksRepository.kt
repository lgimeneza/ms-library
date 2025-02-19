package io.demo.mslibrary.infrastructure.repositories

import io.demo.mslibrary.domain.Book
import io.demo.mslibrary.domain.BooksRepository
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate

class PostgreSqlBooksRepository(private val jdbcTemplate: NamedParameterJdbcTemplate) : BooksRepository {
    override fun save(book: Book) {
        val query =
            """
            INSERT INTO books (title)
            VALUES (:title)
            """
        val namedParameters = mapOf("title" to book.title)
        jdbcTemplate.update(query, namedParameters)
    }
}
