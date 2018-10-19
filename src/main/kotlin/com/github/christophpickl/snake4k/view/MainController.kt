package com.github.christophpickl.snake4k.view

import com.github.christophpickl.snake4k.model.GameState
import com.github.christophpickl.snake4k.model.State
import com.google.common.eventbus.EventBus
import javafx.scene.control.Alert
import javafx.scene.control.ButtonType
import tornadofx.*

class MainController : Controller() {
    // TODO mimick Swing behaviour of closing window

    private val bus: EventBus by di()
    private val state: State by di()

    // TODO on quit getting "Not on FX application thread; currentThread = JavaFX Application Thread"
    init {
        subscribe<GameOverEvent> { event ->
            //            runAsync { } ui {
            alert(
                type = Alert.AlertType.INFORMATION,
                owner = primaryStage,
                title = "",
                header = "Game over",
                content = "${event.detailMessage}\n" +
                    "Fruits eaten: ${event.fruitsEaten}\n" +
                    "Time survived: ${formatTime(event.secondsPlayed)}",
                buttons = *arrayOf(ButtonType("Restart"), ButtonType("Quit")),
                actionFn = {
                    when (it.text) {
                        "Restart" -> bus.post(RestartEvent)
                        "Quit" -> bus.post(QuitEvent)
                    }
                }
            )
//            }
        }
        subscribe<PauseEvent> {
            if (state.gameState == GameState.NotRunning) {
                throw IllegalStateException("Game not running, nothing to pause :(")
            }
            state.gameState = if (state.gameState == GameState.Running) GameState.Paused else GameState.Running
        }
        subscribe<ExceptionEvent> {
            val e = it.exception
            alert(
                type = Alert.AlertType.ERROR,
                owner = primaryStage,
                title = "",
                header = "Unhandled Exception!",
                content = "${e.javaClass.simpleName}: ${e.message}",
                buttons = *arrayOf(ButtonType.OK)
            )
        }
    }

    private fun formatTime(seconds: Int) =
        "${if (seconds >= 60) "${seconds / 60} Minutes " else ""}${seconds % 60} Second${if (seconds == 1) "" else "s"}"

}
