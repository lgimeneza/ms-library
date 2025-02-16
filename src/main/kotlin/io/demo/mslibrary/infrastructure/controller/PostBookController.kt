package io.demo.mslibrary.infrastructure.controller

import org.springframework.http.HttpStatus.CREATED
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RestController

@RestController
class PostBookController {
    @PostMapping("/books")
    fun postBook(): ResponseEntity<Unit> {
        return ResponseEntity.status(CREATED).build()
    }
}