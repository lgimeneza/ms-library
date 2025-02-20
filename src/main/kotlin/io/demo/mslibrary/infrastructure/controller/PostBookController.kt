package io.demo.mslibrary.infrastructure.controller

import io.demo.mslibrary.application.RegisterBook
import io.demo.mslibrary.application.RegisterBookCommand
import org.springframework.http.HttpStatus.CREATED
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController

@RestController
class PostBookController(private val registerBook: RegisterBook) {
    @PostMapping("/books")
    fun postBook(@RequestBody postBookRequest: PostBookRequest): ResponseEntity<Unit> {
        val registerBookCommand = RegisterBookCommand(postBookRequest.title)
        registerBook.execute(registerBookCommand)
        return ResponseEntity.status(CREATED).build()
    }
}
