package io.demo.mslibrary.application

import io.demo.mslibrary.domain.Book

data class BookDto(
    val id: String,
    val title: String,
    val author: String,
    val category: String,
    val publishedYear: Int,
    val isbn: String?
)

fun Book.toDto() =
    BookDto(
        id = id.value.toString(),
        title = title.value,
        author = author.value,
        category = category.value,
        publishedYear = publishedYear.value,
        isbn = isbn?.value)
