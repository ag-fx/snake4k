package com.github.christophpickl.snake4k.view

import com.google.common.eventbus.EventBus
import com.google.common.eventbus.Subscribe
import tornadofx.*

class FxBridge : Controller() {

    private val bus: EventBus by di()

    init {
        bus.register(this)
        subscribe<RestartEvent> {
            bus.post(it)
        }
        subscribe<QuitEvent> {
            bus.post(it)
        }
    }

    @Subscribe
    fun onGameOverEvent(event: GameOverEvent) {
        fire(event)
    }
}
