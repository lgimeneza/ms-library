package io.demo.mslibrary.application

import io.demo.mslibrary.domain.Book
import io.demo.mslibrary.domain.BooksRepository

class SaveBook(private val booksRepository: BooksRepository) {
    fun execute(saveBookCommand: SaveBookCommand) {
        val book = Book(saveBookCommand.title)
        booksRepository.save(book)
    }
}
