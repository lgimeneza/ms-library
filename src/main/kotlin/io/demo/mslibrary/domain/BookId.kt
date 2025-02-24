package io.demo.mslibrary.domain

import java.util.UUID

data class BookId(val value: UUID) {
    companion object {
        fun generate() = BookId(UUID.randomUUID())
    }
}
