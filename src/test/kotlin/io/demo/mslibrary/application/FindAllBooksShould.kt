package io.demo.mslibrary.application

import com.nhaarman.mockitokotlin2.doReturn
import com.nhaarman.mockitokotlin2.mock
import io.demo.mslibrary.domain.Author
import io.demo.mslibrary.domain.Book
import io.demo.mslibrary.domain.BookId
import io.demo.mslibrary.domain.BooksRepository
import io.demo.mslibrary.domain.Category
import io.demo.mslibrary.domain.Isbn
import io.demo.mslibrary.domain.PublishedYear
import io.demo.mslibrary.domain.Title
import java.util.UUID
import kotlin.test.assertEquals
import kotlin.test.assertTrue
import org.junit.jupiter.api.Test

class FindAllBooksShould {
    @Test
    fun `find all books`() {
        val expectedBooks =
            listOf(
                BookDto(
                    id = UUID.randomUUID().toString(),
                    title = "The Shining",
                    author = "Stephen King",
                    category = "THRILLER",
                    publishedYear = 1977,
                    isbn = "978-0-385-12167-5"),
                BookDto(
                    id = UUID.randomUUID().toString(),
                    title = "It",
                    author = "Stephen King",
                    category = "HORROR",
                    publishedYear = 1986,
                    isbn = "978-0-670-81302-5"),
                BookDto(
                    id = UUID.randomUUID().toString(),
                    title = "1984",
                    author = "George Orwell",
                    category = "SCIENCE_FICTION",
                    publishedYear = 1949,
                    isbn = "978-0-452-28423-4"))

        val booksRepository =
            mock<BooksRepository> {
                on { findAll() } doReturn
                    expectedBooks.map { bookDto ->
                        Book(
                            BookId(UUID.fromString(bookDto.id)),
                            Title(bookDto.title),
                            Author(bookDto.author),
                            Category(bookDto.category),
                            PublishedYear(bookDto.publishedYear),
                            bookDto.isbn?.let { Isbn(it) })
                    }
            }

        val findAllBooks = FindAllBooks(booksRepository)

        val actualBooks = findAllBooks.execute()

        assertEquals(expectedBooks, actualBooks)
    }

    @Test
    fun `return empty list when no books exist`() {
        val booksRepository = mock<BooksRepository> { on { findAll() } doReturn emptyList() }

        val findAllBooks = FindAllBooks(booksRepository)

        val actualBooks = findAllBooks.execute()

        assertTrue(actualBooks.isEmpty())
    }
}
