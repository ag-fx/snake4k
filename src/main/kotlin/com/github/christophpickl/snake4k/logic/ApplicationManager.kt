package com.github.christophpickl.snake4k.logic

import com.github.christophpickl.snake4k.QuitEvent
import com.google.common.eventbus.DeadEvent
import com.google.common.eventbus.EventBus
import com.google.common.eventbus.Subscribe
import javafx.application.Platform
import mu.KotlinLogging
import javax.inject.Inject

class ApplicationManager @Inject constructor(
    private val engine: GameEngine,
    bus: EventBus
) {

    private val log = KotlinLogging.logger {}

    init {
        bus.register(this)
    }

    fun start() {
        log.debug { "Start game" }
        engine.restart()
    }

    @Suppress("UNUSED_PARAMETER")
    @Subscribe
    fun onQuitEvent(event: QuitEvent = QuitEvent) {
        log.debug { "Received quit event" }
        engine.stop()
        Platform.exit()
    }

    @Subscribe
    fun onDeadEvent(event: DeadEvent) {
        log.warn { "Unhandled event received: $event" }
    }

    @Subscribe
    fun onAnyEvent(event: Any) {
        log.info { "Event dispatched: $event" }
    }

}
