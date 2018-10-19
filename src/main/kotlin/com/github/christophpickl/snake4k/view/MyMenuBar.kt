package com.github.christophpickl.snake4k.view

import com.github.christophpickl.snake4k.model.GameState
import com.github.christophpickl.snake4k.model.StateModel
import javafx.scene.control.MenuBar
import javafx.scene.input.KeyCode
import javafx.scene.input.KeyCodeCombination
import javafx.scene.input.KeyCombination
import tornadofx.*

class MyMenuBar(
    view: View,
    stateModel: StateModel
) : MenuBar() {
    init {
        isUseSystemMenuBar = true

        menu("Application") {
            item(
                name = "Restart",
                keyCombination = KeyCodeCombination(KeyCode.R, KeyCombination.META_DOWN)
            ).action {
                view.fire(RestartEvent)
            }
            item(
                name = "Pause",
                keyCombination = KeyCodeCombination(KeyCode.P, KeyCombination.META_DOWN)
            ).apply {
                stateModel.gameState.onChange {
                    isDisable = (it == GameState.NotRunning)
                }
                action {
                    view.fire(PauseEvent)
                }
            }
            item(
                name = "Quit"
            ).action {
                view.fire(QuitEvent)
            }
        }
    }
}
