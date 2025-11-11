package io.demo.mslibrary.infrastructure.controller

import io.demo.mslibrary.application.FindBook
import io.demo.mslibrary.application.FindBookQuery
import io.demo.mslibrary.domain.exceptions.BookNotFoundException
import java.net.HttpURLConnection.HTTP_OK
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RestController

@RestController
class GetBookController(private val findBook: FindBook) {
    @GetMapping("/books/{id}")
    fun getBook(@PathVariable id: String): ResponseEntity<GetBookResponse> {
        val findBookQuery = FindBookQuery(id)
        return try {
            val bookDto = findBook.execute(findBookQuery)
            ResponseEntity.status(HTTP_OK).body(bookDto.toGetBookResponse())
        } catch (ex: BookNotFoundException) {
            ResponseEntity.notFound().build()
        }
    }
}
