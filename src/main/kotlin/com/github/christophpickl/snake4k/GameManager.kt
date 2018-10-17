package com.github.christophpickl.snake4k

import com.github.christophpickl.snake4k.view.KeyboardWatcher
import com.github.christophpickl.snake4k.view.Window
import com.google.common.eventbus.DeadEvent
import com.google.common.eventbus.EventBus
import com.google.common.eventbus.Subscribe
import java.awt.event.WindowAdapter
import java.awt.event.WindowEvent
import javax.inject.Inject

class GameManager @Inject constructor(
    private val window: Window,
    private val keyboard: KeyboardWatcher,
    private val engine: GameEngine,
    bus: EventBus
) {

    init {
        bus.register(this)
    }

    fun start() {
        window.addWindowListener(object : WindowAdapter() {
            override fun windowClosing(e: WindowEvent) {
                onQuitEvent()
            }
        })
        window.addKeyListener(keyboard)
        window.isVisible = true
        onRestartEvent()
    }

    @Suppress("UNUSED_PARAMETER")
    @Subscribe
    fun onRestartEvent(event: RestartEvent = RestartEvent) {
        Log.debug { "Restart game." }
        engine.restart()
    }

    @Suppress("UNUSED_PARAMETER")
    @Subscribe
    fun onQuitEvent(event: QuitEvent = QuitEvent) {
        Log.debug { "Quit application ..." }
        engine.stop()
        window.isVisible = false
        window.dispose()
    }

    @Suppress("UNUSED_PARAMETER")
    @Subscribe
    fun onDeadEvent(event: DeadEvent) {
        Log.debug { "Warning: Unused event found: $event" }
    }

}

object RestartEvent
object QuitEvent
