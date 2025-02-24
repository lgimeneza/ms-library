package io.demo.mslibrary.application

data class RegisterBookCommand(
    val title: String,
    val author: String,
    val category: String,
    val publishedYear: Int,
    val isbn: String?
)
