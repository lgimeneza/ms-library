package io.demo.mslibrary.infrastructure.repositories

import io.demo.mslibrary.domain.Author
import io.demo.mslibrary.domain.Book
import io.demo.mslibrary.domain.BookId
import io.demo.mslibrary.domain.BooksRepository
import io.demo.mslibrary.domain.Category
import io.demo.mslibrary.domain.Isbn
import io.demo.mslibrary.domain.PublishedYear
import io.demo.mslibrary.domain.Title
import io.demo.mslibrary.domain.exceptions.BookNotFoundException
import java.sql.ResultSet
import java.util.UUID
import org.springframework.dao.EmptyResultDataAccessException
import org.springframework.jdbc.core.RowMapper
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

        return try {
            jdbcTemplate.queryForObject(query, namedParameters, mapResultSetToBook()) as Book
        } catch (exception: EmptyResultDataAccessException) {
            throw BookNotFoundException("Book with id ${id.value} not found")
        }
    }

    override fun findAll(): List<Book> {
        val query =
            """
            SELECT id, title, author, category, published_year, isbn
            FROM books
        """

        return jdbcTemplate.query(query, mapResultSetToBook())
    }

    override fun delete(id: BookId) {
        val query =
            """
            DELETE FROM books
            WHERE id = :id
        """

        val namedParameters = MapSqlParameterSource()
        namedParameters.addValue("id", id.value)

        jdbcTemplate.update(query, namedParameters)
    }

    private fun mapResultSetToBook(): RowMapper<Book> {
        return RowMapper { rs: ResultSet, _: Int ->
            val id = rs.getString("id")
            val title = rs.getString("title")
            val author = rs.getString("author")
            val category = rs.getString("category")
            val publishedYear = rs.getInt("published_year")
            val isbn = rs.getString("isbn")

            Book(
                BookId(UUID.fromString(id)),
                Title(title),
                Author(author),
                Category(category),
                PublishedYear(publishedYear),
                isbn?.let { Isbn(it) })
        }
    }
}
