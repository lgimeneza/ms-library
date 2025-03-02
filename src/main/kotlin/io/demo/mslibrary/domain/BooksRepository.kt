package io.demo.mslibrary.domain

interface BooksRepository {
    fun save(book: Book)

    fun find(id: BookId): Book
}
