package io.demo.mslibrary.application

import com.nhaarman.mockitokotlin2.argumentCaptor
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.verify
import io.demo.mslibrary.domain.Book
import io.demo.mslibrary.domain.BooksRepository
import kotlin.test.assertEquals
import org.junit.jupiter.api.Test

class SaveBookShould {
    @Test
    fun `should save a book`() {
        val title = "The Stand"
        val booksRepository = mock<BooksRepository>()
        val saveBook = SaveBook(booksRepository)
        val saveBookCommand = SaveBookCommand(title)

        saveBook.execute(saveBookCommand)

        val bookCaptor = argumentCaptor<Book>()
        verify(booksRepository).save(bookCaptor.capture())
        assertEquals(title, bookCaptor.firstValue.title)
    }
}
