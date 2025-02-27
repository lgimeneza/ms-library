package io.demo.mslibrary.infrastructure.helper

import java.io.File
import java.lang.System.setProperty
import org.testcontainers.containers.ComposeContainer
import org.testcontainers.containers.wait.strategy.Wait
import org.testcontainers.containers.wait.strategy.WaitAllStrategy

class DockerComposeHelper {
    companion object {
        private const val POSTGRESQL = "postgresql"
        private const val POSTGRESQL_PORT = 5432

        private const val KAFKA = "kafka"
        private const val KAFKA_PORT = 9092
        private const val INIT_KAFKA_SERVICE = "init-kafka"

        private const val ZOOKEEPER = "zookeeper"
        private const val ZOOKEEPER_PORT = 2181

        fun start(): ComposeContainer {
            val container =
                ComposeContainer(arrayListOf(File("docker-compose.yml")))
                    .apply { withLocalCompose(true) }
                    .apply {
                        withExposedService(
                            POSTGRESQL,
                            POSTGRESQL_PORT,
                            WaitAllStrategy(WaitAllStrategy.Mode.WITH_INDIVIDUAL_TIMEOUTS_ONLY)
                                .apply { withStrategy(Wait.forListeningPort()) }
                                .apply {
                                    withStrategy(
                                        Wait.forLogMessage(".*database system is ready to accept connections.*", 1))
                                })
                    }
                    .apply {
                        withExposedService(
                            KAFKA,
                            KAFKA_PORT,
                            WaitAllStrategy(WaitAllStrategy.Mode.WITH_INDIVIDUAL_TIMEOUTS_ONLY)
                                .apply { withStrategy(Wait.forListeningPort()) }
                                .apply { withStrategy(Wait.forLogMessage(".*Startup complete.*", 1)) })
                    }
                    .apply {
                        withExposedService(
                            ZOOKEEPER,
                            ZOOKEEPER_PORT,
                            WaitAllStrategy(WaitAllStrategy.Mode.WITH_INDIVIDUAL_TIMEOUTS_ONLY)
                                .apply { withStrategy(Wait.forListeningPort()) }
                                .apply { withStrategy(Wait.forLogMessage(".*binding to port.*", 1)) })
                    }
                    .apply {
                        waitingFor(
                            INIT_KAFKA_SERVICE,
                            WaitAllStrategy(WaitAllStrategy.Mode.WITH_INDIVIDUAL_TIMEOUTS_ONLY)
                                .apply { withStrategy(Wait.forListeningPort()) }
                                .apply {
                                    withStrategy(
                                        Wait.forLogMessage(".*Successfully created the following topics:.*", 1))
                                })
                    }
            container.start()
            return container
        }

        fun setSystemProperties(composeContainer: ComposeContainer) {
            val postgresHost = composeContainer.getServiceHost(POSTGRESQL, POSTGRESQL_PORT)
            val postgresPort = composeContainer.getServicePort(POSTGRESQL, POSTGRESQL_PORT)
            setProperty("postgres.host", postgresHost)
            setProperty("postgres.port", postgresPort.toString())
            setProperty(
                "spring.cloud.stream.kafka.binder.brokers",
                "${composeContainer.getServiceHost(KAFKA, KAFKA_PORT)}:" +
                    "${composeContainer.getServicePort(KAFKA, KAFKA_PORT)}")
        }
    }
}
