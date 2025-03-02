package io.demo.mslibrary.infrastructure.controller

import io.demo.mslibrary.application.RegisterBook
import io.demo.mslibrary.application.RegisterBookCommand
import java.net.HttpURLConnection.HTTP_CREATED
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController

@RestController
class PostBookController(private val registerBook: RegisterBook) {
    @PostMapping("/books")
    fun postBook(@RequestBody postBookRequest: PostBookRequest): ResponseEntity<Unit> {
        val registerBookCommand =
            RegisterBookCommand(
                postBookRequest.title,
                postBookRequest.author,
                postBookRequest.category,
                postBookRequest.publishedYear,
                postBookRequest.isbn)
        registerBook.execute(registerBookCommand)
        return ResponseEntity.status(HTTP_CREATED).build()
    }
}
