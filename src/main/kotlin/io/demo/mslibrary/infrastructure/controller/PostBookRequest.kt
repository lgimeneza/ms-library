package io.demo.mslibrary.infrastructure.controller

data class PostBookRequest(
    val title: String,
    val author: String,
    val category: String,
    val publishedYear: Int,
    val isbn: String
)
