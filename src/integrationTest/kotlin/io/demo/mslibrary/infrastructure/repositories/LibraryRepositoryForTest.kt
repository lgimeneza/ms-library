package io.demo.mslibrary.infrastructure.repositories

import org.springframework.jdbc.core.namedparam.MapSqlParameterSource
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate

class LibraryRepositoryForTest(private val jdbcTemplate: NamedParameterJdbcTemplate) {
    fun exists(title: String, author: String, category: String, publishedYear: Int, isbn: String): Boolean {
        val query =
            """
            SELECT COUNT(*) 
            FROM books 
            WHERE title = :title
            AND author = :author
            AND category = :category
            AND published_year = :publishedYear
            AND isbn = :isbn
            """
        val namedParameters = MapSqlParameterSource()
        namedParameters.addValue("title", title)
        namedParameters.addValue("author", author)
        namedParameters.addValue("category", category)
        namedParameters.addValue("publishedYear", publishedYear)
        namedParameters.addValue("isbn", isbn)

        return jdbcTemplate.queryForObject(query, namedParameters, Int::class.java)!! > 0
    }
}
