package com.github.christophpickl.snake4k.view

import com.google.common.eventbus.EventBus
import tornadofx.*

class FxBridge : Controller() {

    private val bus: EventBus by di()

    init {
        subscribe<RestartEvent> {
            bus.post(it)
        }
        subscribe<QuitEvent> {
            bus.post(it)
        }
    }
}
