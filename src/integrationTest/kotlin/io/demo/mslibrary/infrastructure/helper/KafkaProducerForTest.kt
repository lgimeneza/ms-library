package io.demo.mslibrary.infrastructure.helper

import java.util.Properties
import org.apache.kafka.clients.producer.KafkaProducer
import org.apache.kafka.clients.producer.Producer
import org.apache.kafka.clients.producer.ProducerConfig
import org.apache.kafka.clients.producer.ProducerRecord
import org.apache.kafka.common.serialization.StringSerializer

class KafkaProducerForTest(bootstrapServers: String) {
    private val producer: Producer<String, String>

    init {
        val config = Properties()
        config[ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG] = StringSerializer::class.java.name
        config[ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG] = StringSerializer::class.java.name
        config[ProducerConfig.BOOTSTRAP_SERVERS_CONFIG] = bootstrapServers
        producer = KafkaProducer(config)
    }

    fun send(topic: String?, body: String?) {
        producer.send(ProducerRecord(topic, body)).get()
        producer.flush()
    }
}
