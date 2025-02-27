package io.demo.mslibrary.application

import com.nhaarman.mockitokotlin2.argumentCaptor
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.verify
import io.demo.mslibrary.domain.Book
import io.demo.mslibrary.domain.BookRegisteredEvent
import io.demo.mslibrary.domain.BooksEventsPublisher
import io.demo.mslibrary.domain.BooksRepository
import io.demo.mslibrary.domain.DomainEvent
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import org.junit.jupiter.api.Test

class RegisterBookShould {
    @Test
    fun `register a book`() {
        val title = "The Stand"
        val author = "Stephen King"
        val category = "FANTASY"
        val publishedYear = 1978
        val isbn = "978-0-385-12168-2"

        val booksRepository = mock<BooksRepository>()
        val booksEventsPublisher = mock<BooksEventsPublisher>()
        val registerBook = RegisterBook(booksRepository, booksEventsPublisher)
        val registerBookCommand = RegisterBookCommand(title, author, category, publishedYear, isbn)

        registerBook.execute(registerBookCommand)

        val bookCaptor = argumentCaptor<Book>()
        verify(booksRepository).save(bookCaptor.capture())
        assertNotNull(bookCaptor.firstValue.id)
        assertEquals(title, bookCaptor.firstValue.title.value)
        assertEquals(author, bookCaptor.firstValue.author.value)
        assertEquals(category, bookCaptor.firstValue.category.value)
        assertEquals(publishedYear, bookCaptor.firstValue.publishedYear.value)
        assertEquals(isbn, bookCaptor.firstValue.isbn?.value)

        val domainEventsCaptor = argumentCaptor<ArrayList<DomainEvent>>()
        verify(booksEventsPublisher).publish(domainEventsCaptor.capture())
        assertEquals(1, domainEventsCaptor.firstValue.size)

        val bookRegisteredEvent =
            domainEventsCaptor.firstValue.find { it is BookRegisteredEvent } as BookRegisteredEvent
        assertEquals(bookCaptor.firstValue, bookRegisteredEvent.book)
        assertNotNull(bookRegisteredEvent.id)
        assertNotNull(bookRegisteredEvent.occurredOn)
    }
}
