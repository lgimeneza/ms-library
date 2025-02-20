package io.demo.mslibrary.application

import io.demo.mslibrary.domain.Book
import io.demo.mslibrary.domain.BooksRepository

class RegisterBook(private val booksRepository: BooksRepository) {
    fun execute(registerBookCommand: RegisterBookCommand) {
        val book = Book(registerBookCommand.title)
        booksRepository.save(book)
    }
}
