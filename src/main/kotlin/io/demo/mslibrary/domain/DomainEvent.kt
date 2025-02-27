package io.demo.mslibrary.domain

import java.time.OffsetDateTime
import java.util.UUID

abstract class DomainEvent {
    val id = UUID.randomUUID().toString()
    val occurredOn = OffsetDateTime.now()
}
