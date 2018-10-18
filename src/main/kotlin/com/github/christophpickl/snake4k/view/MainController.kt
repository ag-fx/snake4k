package com.github.christophpickl.snake4k.view

import com.google.common.eventbus.EventBus
import javafx.scene.control.Alert
import javafx.scene.control.ButtonType
import tornadofx.*

class MainController : Controller() {
    // TODO mimick Swing behaviour of closing window

    private val bus: EventBus by di()

    // TODO on quit getting "Not on FX application thread; currentThread = JavaFX Application Thread"
    init {
        subscribe<GameOverEvent> { event ->
            //            runAsync { } ui {
            alert(
                type = Alert.AlertType.WARNING,
                owner = primaryStage,
                title = "",
                header = "Game over!",
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
        "${if (seconds >= 60) "${seconds / 60}min " else ""}${seconds % 60}sec"

}
