package io.demo.mslibrary.application

import com.nhaarman.mockitokotlin2.any
import com.nhaarman.mockitokotlin2.doReturn
import com.nhaarman.mockitokotlin2.doThrow
import com.nhaarman.mockitokotlin2.mock
import io.demo.mslibrary.domain.Author
import io.demo.mslibrary.domain.Book
import io.demo.mslibrary.domain.BookId
import io.demo.mslibrary.domain.BooksRepository
import io.demo.mslibrary.domain.Category
import io.demo.mslibrary.domain.Isbn
import io.demo.mslibrary.domain.PublishedYear
import io.demo.mslibrary.domain.Title
import io.demo.mslibrary.domain.exceptions.BookNotFoundException
import java.util.UUID
import kotlin.test.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows

class FindBookShould {
    @Test
    fun `find a book`() {
        val expectedBook =
            BookDto(
                id = UUID.randomUUID().toString(),
                title = "The Shining",
                author = "Stephen King",
                category = "THRILLER",
                publishedYear = 1977,
                isbn = "978-0-385-12167-5")

        val booksRepository =
            mock<BooksRepository> {
                on { find(BookId(UUID.fromString(expectedBook.id))) } doReturn
                    Book(
                        BookId(UUID.fromString(expectedBook.id)),
                        Title(expectedBook.title),
                        Author(expectedBook.author),
                        Category(expectedBook.category),
                        PublishedYear(expectedBook.publishedYear),
                        expectedBook.isbn?.let { Isbn(it) })
            }
        val findBookQuery = FindBookQuery(expectedBook.id)
        val findBook = FindBook(booksRepository)

        val actualBook = findBook.execute(findBookQuery)

        assertEquals(expectedBook, actualBook)
    }

    @Test
    fun `throw exception if the book does not exist`() {
        val booksRepository = mock<BooksRepository>() { on { find(any()) } doThrow BookNotFoundException("not found") }

        val findBookQuery = FindBookQuery(UUID.randomUUID().toString())
        val findBook = FindBook(booksRepository)

        assertThrows<BookNotFoundException> { findBook.execute(findBookQuery) }
    }
}
