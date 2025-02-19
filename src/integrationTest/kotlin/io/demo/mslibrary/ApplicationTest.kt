package io.demo.mslibrary

import io.demo.mslibrary.infrastructure.Application
import io.demo.mslibrary.infrastructure.helper.ContainersInitializer
import org.junit.jupiter.api.Test
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ContextConfiguration

@SpringBootTest(classes = [Application::class])
@ContextConfiguration(initializers = [ContainersInitializer::class])
class ApplicationTest {
    @Test fun contextLoads() {}
}
