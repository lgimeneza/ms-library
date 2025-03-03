package io.demo.mslibrary.application

import io.demo.mslibrary.domain.BookId
import io.demo.mslibrary.domain.BooksRepository
import java.util.UUID

class FindBook(private val booksRepository: BooksRepository) {
    fun execute(findBookQuery: FindBookQuery): BookDto? {
        val bookId = BookId(UUID.fromString(findBookQuery.id))
        val book = booksRepository.find(bookId)
        return book?.toDto()
    }
}
