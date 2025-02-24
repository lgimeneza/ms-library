package io.demo.mslibrary.infrastructure.repositories

import io.demo.mslibrary.domain.Book
import io.demo.mslibrary.domain.BooksRepository
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate

class PostgreSqlBooksRepository(private val jdbcTemplate: NamedParameterJdbcTemplate) : BooksRepository {
    override fun save(book: Book) {
        val query =
            """
            INSERT INTO books (id, title, author, category, published_year, isbn)
            VALUES (:id, :title, :author, :category, :publishedYear, :isbn)
        """

        val namedParameters = MapSqlParameterSource()
        namedParameters.addValue("id", book.id.value)
        namedParameters.addValue("title", book.title.value)
        namedParameters.addValue("author", book.author.value)
        namedParameters.addValue("category", book.category.value)
        namedParameters.addValue("publishedYear", book.publishedYear.value)
        namedParameters.addValue("isbn", book.isbn?.value)

        jdbcTemplate.update(query, namedParameters)
    }
}
