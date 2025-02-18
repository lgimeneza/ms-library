package io.demo.mslibrary.infrastructure.repositories

import io.demo.mslibrary.domain.Book
import io.demo.mslibrary.domain.BooksRepository

class InMemoryBooksRepository : BooksRepository {
    private val books = mutableListOf<Book>()

    override fun save(book: Book) {
        books.add(book)
    }
}
