package io.demo.mslibrary.domain

interface BooksEventsPublisher {
    fun publish(events: List<DomainEvent>)
}
