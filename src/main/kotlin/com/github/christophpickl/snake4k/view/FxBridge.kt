package com.github.christophpickl.snake4k.view

import com.google.common.eventbus.EventBus
import com.google.common.eventbus.Subscribe
import tornadofx.*

class FxBridge : Controller() {

    private val bus: EventBus by di()

    init {
        bus.register(this)
        subscribe<RestartFxEvent> {
            if (!it.wasDispatchedInGuice) {
                it.wasDispatchedInGuice = true
                bus.post(RestartGuiceEvent(wasDispatchedInFx = true))
            }
        }
        subscribe<QuitEvent> {
            bus.post(it)
        }
    }

    @Subscribe
    fun onGameOverEvent(event: GameOverEvent) {
        fire(event)
    }

    @Subscribe
    fun onExceptionEvent(event: ExceptionEvent) {
        fire(event)
    }

    @Subscribe
    fun onRestartGuiceEvent(event: RestartGuiceEvent) {
        if (!event.wasDispatchedInFx) {
            event.wasDispatchedInFx = true
            fire(RestartFxEvent(wasDispatchedInGuice = true))
        }
    }

}
