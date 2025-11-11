package io.demo.mslibrary.infrastructure.controller

import io.demo.mslibrary.application.BookDto

data class GetBooksResponse(
    val id: String,
    val title: String,
    val author: String,
    val category: String,
    val publishedYear: Int,
    val isbn: String?
)

fun BookDto.toGetBooksResponse() =
    GetBooksResponse(
        id = id, title = title, author = author, category = category, publishedYear = publishedYear, isbn = isbn)
