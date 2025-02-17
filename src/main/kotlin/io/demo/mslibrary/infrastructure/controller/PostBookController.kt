package io.demo.mslibrary.infrastructure.controller

import io.demo.mslibrary.application.SaveBook
import io.demo.mslibrary.application.SaveBookCommand
import org.springframework.http.HttpStatus.CREATED
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController

@RestController
class PostBookController(
    private val saveBook: SaveBook
) {
    @PostMapping("/books")
    fun postBook(
        @RequestBody postBookRequest: PostBookRequest
    ): ResponseEntity<Unit> {
        val saveBookCommand = SaveBookCommand(postBookRequest.title)
        saveBook.execute(saveBookCommand)
        return ResponseEntity.status(CREATED).build()
    }
}