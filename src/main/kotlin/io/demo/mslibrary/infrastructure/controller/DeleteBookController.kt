package io.demo.mslibrary.infrastructure.controller

import io.demo.mslibrary.application.DeleteBook
import io.demo.mslibrary.application.DeleteBookCommand
import io.demo.mslibrary.domain.exceptions.BookNotFoundException
import java.net.HttpURLConnection.HTTP_NOT_FOUND
import java.net.HttpURLConnection.HTTP_NO_CONTENT
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RestController

@RestController
class DeleteBookController(private val deleteBook: DeleteBook) {
    @DeleteMapping("/books/{id}")
    fun deleteBook(@PathVariable id: String): ResponseEntity<Void> {
        val deleteBookCommand = DeleteBookCommand(id)
        return try {
            deleteBook.execute(deleteBookCommand)
            ResponseEntity.status(HTTP_NO_CONTENT).build()
        } catch (_: BookNotFoundException) {
            ResponseEntity.status(HTTP_NOT_FOUND).build()
        }
    }
}
