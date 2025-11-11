package io.demo.mslibrary.application

import io.demo.mslibrary.domain.BooksRepository

class FindAllBooks(private val booksRepository: BooksRepository) {
    fun execute(): List<BookDto> {
        val books = booksRepository.findAll()
        return books.map { it.toDto() }
    }
}
