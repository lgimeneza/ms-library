package io.demo.mslibrary.application

import com.nhaarman.mockitokotlin2.doReturn
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.verify
import com.nhaarman.mockitokotlin2.whenever
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
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows

class DeleteBookShould {
    @Test
    fun `delete a book when it exists`() {
        val bookId = UUID.randomUUID()
        val book =
            Book(
                BookId(bookId),
                Title("It"),
                Author("Stephen King"),
                Category("HORROR"),
                PublishedYear(1986),
                Isbn("978-0-670-81302-5"))

        val booksRepository = mock<BooksRepository>()
        whenever(booksRepository.find(BookId(bookId))).doReturn(book)

        val deleteBook = DeleteBook(booksRepository)
        val deleteBookCommand = DeleteBookCommand(bookId.toString())

        deleteBook.execute(deleteBookCommand)

        verify(booksRepository).delete(BookId(bookId))
    }

    @Test
    fun `throw exception when book does not exist`() {
        val bookId = UUID.randomUUID()
        val booksRepository = mock<BooksRepository>()
        whenever(booksRepository.find(BookId(bookId))).thenThrow(BookNotFoundException("not found"))

        val deleteBook = DeleteBook(booksRepository)
        val deleteBookCommand = DeleteBookCommand(bookId.toString())

        assertThrows<BookNotFoundException> { deleteBook.execute(deleteBookCommand) }
    }
}
