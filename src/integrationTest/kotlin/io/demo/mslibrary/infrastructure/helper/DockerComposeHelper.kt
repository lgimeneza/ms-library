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
            container.start()
            return container
        }

        fun setSystemProperties(composeContainer: ComposeContainer) {
            val postgresHost = composeContainer.getServiceHost(POSTGRESQL, POSTGRESQL_PORT)
            val postgresPort = composeContainer.getServicePort(POSTGRESQL, POSTGRESQL_PORT)
            setProperty("postgres.host", postgresHost)
            setProperty("postgres.port", postgresPort.toString())
        }
    }
}
