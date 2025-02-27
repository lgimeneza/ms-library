package io.demo.mslibrary.infrastructure.streams

import com.fasterxml.jackson.databind.ObjectMapper
import io.demo.mslibrary.domain.BookRegisteredEvent
import io.demo.mslibrary.domain.BooksEventsPublisher
import io.demo.mslibrary.domain.DomainEvent
import io.demo.mslibrary.domain.PublishEventException
import java.nio.charset.StandardCharsets
import org.springframework.cloud.stream.function.StreamBridge
import org.springframework.integration.support.MessageBuilder
import org.springframework.kafka.support.KafkaHeaders.KEY

class KafkaBooksEventsProducer(private val streamBridge: StreamBridge, private val objectMapper: ObjectMapper) :
    BooksEventsPublisher {

    companion object {
        private const val BINDING_NAME = "books-out-0"
    }

    override fun publish(events: List<DomainEvent>) {
        events.forEach(this::produceEvent)
    }

    private fun produceEvent(domainEvent: DomainEvent) {
        val streamEvent = prepareStreamEvent(domainEvent)
        val message =
            MessageBuilder.withPayload(streamEvent.jsonBody.toByteArray(StandardCharsets.UTF_8))
                .setHeader(KEY, streamEvent.key)
                .build()
        val success =
            try {
                streamBridge.send(BINDING_NAME, message)
            } catch (e: Throwable) {
                throw PublishEventException("Error publishing event with aggregateId: ${streamEvent.aggregateId}", e)
            }
        if (!success) {
            throw PublishEventException("Failed to publish event with aggregateId: ${streamEvent.aggregateId}")
        }
    }

    private fun prepareStreamEvent(domainEvent: DomainEvent): StreamEvent {
        return when (domainEvent) {
            is BookRegisteredEvent ->
                StreamEvent(
                    jsonBody = objectMapper.writeValueAsString(domainEvent.toEventJsonBody()),
                    key = domainEvent.book.id.value.toString(),
                    aggregateId = domainEvent.book.id.value.toString())
            else -> throw IllegalArgumentException("Unknown event type: $domainEvent")
        }
    }

    private data class StreamEvent(val jsonBody: String, val key: String, val aggregateId: String)
}
