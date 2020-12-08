package com.lowes.auditor.client.infrastructure.frameworks.config

import org.javers.core.Javers
import org.javers.core.JaversBuilder
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration(proxyBeanMethods = false)
internal class JaversModule {
    @Bean
    fun javers(): Javers {
        return JaversBuilder.javers().build()
    }
}
