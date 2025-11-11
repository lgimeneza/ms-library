package io.demo.mslibrary.infrastructure.controller

import io.demo.mslibrary.application.FindAllBooks
import java.net.HttpURLConnection.HTTP_OK
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController

@RestController
class GetBooksController(private val findAllBooks: FindAllBooks) {
    @GetMapping("/books")
    fun getBooks(): ResponseEntity<List<GetBooksResponse>> {
        val booksDto = findAllBooks.execute()
        val response = booksDto.map { it.toGetBooksResponse() }
        return ResponseEntity.status(HTTP_OK).body(response)
    }
}
