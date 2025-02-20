package io.demo.mslibrary.application

import com.nhaarman.mockitokotlin2.argumentCaptor
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.verify
import io.demo.mslibrary.domain.Book
import io.demo.mslibrary.domain.BooksRepository
import kotlin.test.assertEquals
import org.junit.jupiter.api.Test

class RegisterBookShould {
    @Test
    fun `register a book`() {
        val title = "The Stand"
        val booksRepository = mock<BooksRepository>()
        val registerBook = RegisterBook(booksRepository)
        val registerBookCommand = RegisterBookCommand(title)

        registerBook.execute(registerBookCommand)

        val bookCaptor = argumentCaptor<Book>()
        verify(booksRepository).save(bookCaptor.capture())
        assertEquals(title, bookCaptor.firstValue.title)
    }
}
