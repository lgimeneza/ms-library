package io.demo.mslibrary.infrastructure.repositories

import io.demo.mslibrary.domain.Author
import io.demo.mslibrary.domain.Book
import io.demo.mslibrary.domain.BookId
import io.demo.mslibrary.domain.BooksRepository
import io.demo.mslibrary.domain.Category
import io.demo.mslibrary.domain.Isbn
import io.demo.mslibrary.domain.PublishedYear
import io.demo.mslibrary.domain.Title
import java.util.UUID
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

    override fun find(id: BookId): Book {
        val query =
            """
            SELECT id, title, author, category, published_year, isbn
            FROM books
            WHERE id = :id
        """

        val namedParameters = MapSqlParameterSource()
        namedParameters.addValue("id", id.value)

        return jdbcTemplate.queryForObject(query, namedParameters) { rs, _ ->
            Book(
                BookId(UUID.fromString(rs.getString("id"))),
                Title(rs.getString("title")),
                Author(rs.getString("author")),
                Category(rs.getString("category")),
                PublishedYear(rs.getInt("published_year")),
                rs.getString("isbn")?.let { Isbn(it) })
        }!!
    }
}
