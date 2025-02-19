package io.demo.mslibrary.infrastructure.repositories

import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate

class LibraryRepositoryForTest(private val jdbcTemplate: NamedParameterJdbcTemplate) {
    fun exists(title: String): Boolean {
        val query = "SELECT COUNT(*) FROM books WHERE title = :title"
        val namedParameters = mapOf("title" to title)
        return jdbcTemplate.queryForObject(query, namedParameters, Int::class.java)!! > 0
    }
}
