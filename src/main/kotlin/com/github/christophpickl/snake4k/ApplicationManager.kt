package com.github.christophpickl.snake4k

import com.github.christophpickl.snake4k.view.QuitEvent
import com.github.christophpickl.snake4k.view.RestartEvent
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
        log.debug { "Start game." }
        onRestartEvent()
    }

    @Suppress("UNUSED_PARAMETER")
    @Subscribe
    fun onRestartEvent(event: RestartEvent = RestartEvent) {
        log.debug { "Received restart event." }
        engine.restart()
    }

    @Suppress("UNUSED_PARAMETER")
    @Subscribe
    fun onQuitEvent(event: QuitEvent = QuitEvent) {
        log.debug { "Received quit event." }
        engine.stop()
        Platform.exit()
    }

    @Subscribe
    fun onDeadEvent(event: DeadEvent) {
        log.warn { "Unhandled event received: $event" }
    }

}
