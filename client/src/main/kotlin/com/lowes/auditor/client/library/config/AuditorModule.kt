package com.lowes.auditor.client.library.config

import com.lowes.auditor.client.entities.domain.AuditorEventConfig
import com.lowes.auditor.client.entities.interfaces.infrastructure.event.EventPublisher
import com.lowes.auditor.client.entities.interfaces.infrastructure.frameworks.LogProvider
import com.lowes.auditor.client.entities.interfaces.usecase.AuditEventElementFilter
import com.lowes.auditor.client.entities.interfaces.usecase.AuditEventFilter
import com.lowes.auditor.client.infrastructure.frameworks.config.FrameworkModule
import com.lowes.auditor.client.library.service.AuditEventDecoratorService
import com.lowes.auditor.client.library.service.AuditEventFilterService
import com.lowes.auditor.client.library.service.AuditEventGeneratorService
import com.lowes.auditor.client.usecase.ElementAggregatorUseCase
import com.lowes.auditor.client.usecase.ElementFilterUseCase
import com.lowes.auditor.client.usecase.EventFilterUseCase
import com.lowes.auditor.client.usecase.EventLogUseCase
import com.lowes.auditor.client.usecase.LoggingFilterUseCase
import com.lowes.auditor.core.entities.util.JsonObject

class AuditorModule(
    private val auditorEventConfig: AuditorEventConfig,
    private val elementFilters: List<AuditEventElementFilter>,
    private val eventPublisher: EventPublisher,
    private val logProvider: LogProvider
) {

    private val elementAggregatorUseCase: ElementAggregatorUseCase by lazy {
        ElementAggregatorUseCase(FrameworkModule.objectDiffChecker)
    }

    private val elementFilterUseCase: AuditEventElementFilter by lazy {
        ElementFilterUseCase(elementFilters)
    }

    private val eventFilterUseCase: AuditEventFilter by lazy {
        EventFilterUseCase()
    }

    private val loggingFilterUseCase: AuditEventFilter by lazy {
        LoggingFilterUseCase(logProvider)
    }

    private val auditEventFilterService: AuditEventFilterService by lazy {
        AuditEventFilterService(eventFilterUseCase, elementFilterUseCase, loggingFilterUseCase)
    }

    private val auditEventDecoratorService: AuditEventDecoratorService by lazy {
        AuditEventDecoratorService(JsonObject.objectMapper)
    }

    private val eventLogUseCase: EventLogUseCase by lazy {
        EventLogUseCase(FrameworkModule.objectLogGenerator)
    }

    val auditEventGeneratorService: AuditEventGeneratorService by lazy {
        AuditEventGeneratorService(
            elementAggregatorUseCase,
            auditorEventConfig,
            eventPublisher,
            auditEventFilterService,
            auditEventDecoratorService,
            eventLogUseCase
        )
    }
}