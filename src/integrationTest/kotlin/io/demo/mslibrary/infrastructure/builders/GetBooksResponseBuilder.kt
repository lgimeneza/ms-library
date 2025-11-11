package io.demo.mslibrary.infrastructure.builders

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.node.ObjectNode
import java.util.UUID

class GetBooksResponseBuilder {
    private val objectMapper: ObjectMapper = ObjectMapper()
    private val items: MutableList<ObjectNode> = mutableListOf()

    fun addBook(
        id: UUID,
        title: String,
        author: String,
        category: String,
        publishedYear: Int,
        isbn: String
    ): GetBooksResponseBuilder {
        val book = objectMapper.createObjectNode()
        book.put("id", id.toString())
        book.put("title", title)
        book.put("author", author)
        book.put("category", category)
        book.put("publishedYear", publishedYear)
        book.put("isbn", isbn)

        items.add(book)
        return this
    }

    fun build(): String {
        return objectMapper.writeValueAsString(items)
    }
}
