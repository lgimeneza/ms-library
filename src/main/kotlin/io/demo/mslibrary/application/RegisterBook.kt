package io.demo.mslibrary.application

import io.demo.mslibrary.domain.Author
import io.demo.mslibrary.domain.Book
import io.demo.mslibrary.domain.BooksRepository
import io.demo.mslibrary.domain.Category
import io.demo.mslibrary.domain.Isbn
import io.demo.mslibrary.domain.PublishedYear
import io.demo.mslibrary.domain.Title

class RegisterBook(private val booksRepository: BooksRepository) {
    fun execute(registerBookCommand: RegisterBookCommand) {
        val book =
            Book.create(
                Title(registerBookCommand.title),
                Author(registerBookCommand.author),
                Category(registerBookCommand.category),
                PublishedYear(registerBookCommand.publishedYear),
                registerBookCommand.isbn?.let { Isbn(it) })
        booksRepository.save(book)
    }
}
