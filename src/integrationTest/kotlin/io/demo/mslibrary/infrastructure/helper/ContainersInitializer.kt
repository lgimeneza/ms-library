package io.demo.mslibrary.infrastructure.helper

import org.springframework.context.ApplicationContextInitializer
import org.springframework.context.ConfigurableApplicationContext

class ContainersInitializer : ApplicationContextInitializer<ConfigurableApplicationContext> {
    companion object {
        val composeContainer = DockerComposeHelper.start()
    }

    override fun initialize(applicationContext: ConfigurableApplicationContext) {
        DockerComposeHelper.setSystemProperties(composeContainer)
    }
}
