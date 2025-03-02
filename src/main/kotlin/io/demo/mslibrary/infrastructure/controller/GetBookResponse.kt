package io.demo.mslibrary.infrastructure.controller

import io.demo.mslibrary.application.BookDto

data class GetBookResponse(
    val id: String,
    val title: String,
    val author: String,
    val category: String,
    val publishedYear: Int,
    val isbn: String?
)

fun BookDto.toGetBookResponse() =
    GetBookResponse(
        id = id, title = title, author = author, category = category, publishedYear = publishedYear, isbn = isbn)
