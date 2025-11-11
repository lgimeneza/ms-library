package io.demo.mslibrary.application

import io.demo.mslibrary.domain.BookId
import io.demo.mslibrary.domain.BooksRepository
import java.util.UUID

class DeleteBook(private val booksRepository: BooksRepository) {
    fun execute(deleteBookCommand: DeleteBookCommand) {
        val bookId = BookId(UUID.fromString(deleteBookCommand.id))
        booksRepository.find(bookId)
        booksRepository.delete(bookId)
    }
}
