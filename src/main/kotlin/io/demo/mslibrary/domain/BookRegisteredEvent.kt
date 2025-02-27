package io.demo.mslibrary.domain

data class BookRegisteredEvent(val book: Book) : DomainEvent()
