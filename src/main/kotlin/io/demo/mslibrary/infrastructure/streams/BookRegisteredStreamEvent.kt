package io.demo.mslibrary.infrastructure.streams

import io.demo.mslibrary.domain.BookRegisteredEvent
import java.time.OffsetDateTime

data class BookRegisteredStreamEvent(
    val id: String,
    val type: String = EVENT_TYPE,
    val schema: String = EVENT_SCHEMA,
    val version: String = EVENT_VERSION,
    val published: OffsetDateTime,
    val book: BookObject
) {
    companion object {
        const val EVENT_TYPE = "BookRegistered"
        const val EVENT_SCHEMA = "https://demo.io/schemas/book-registered-event.json"
        const val EVENT_VERSION = "1.0"
    }
}

data class BookObject(
    val title: String,
    val author: String,
    val category: String,
    val publishedYear: Int,
    val isbn: String?
)

fun BookRegisteredEvent.toEventJsonBody(): BookRegisteredStreamEvent {
    val (_, title, author, category, publishedYear, isbn) = this.book
    return BookRegisteredStreamEvent(
        id = this.id,
        published = this.occurredOn,
        book =
            BookObject(
                title = title.value,
                author = author.value,
                category = category.value,
                publishedYear = publishedYear.value,
                isbn = isbn?.value))
}
