package com.github.christophpickl.snake4k.view

import javafx.scene.control.MenuBar
import javafx.scene.input.KeyCode
import javafx.scene.input.KeyCodeCombination
import javafx.scene.input.KeyCombination
import javafx.scene.input.KeyEvent
import tornadofx.*

class MainView : View() {
    private val board: Board by di()
    private val keyboard: KeyboardWatcher by di()

    override val root = borderpane {
        title = "Snake4k"
        addEventFilter(KeyEvent.KEY_PRESSED, keyboard)

        paddingAll = 0
        top {
            add(MyMenuBar(this@MainView))
        }
        center {
            add(board)
        }
        bottom {
            label("Fruits eaten: ") // FIXME bind to model
        }
    }

    override fun onDock() {
        // otherwise keyboard listener won't work
        root.requestFocus()
    }
}

class MyMenuBar(
    view: View
) : MenuBar() {
    init {
        isUseSystemMenuBar = true
        menu("Application") {
            item(name = "Restart", keyCombination = KeyCodeCombination(KeyCode.R, KeyCombination.META_DOWN)).action {
                view.fire(RestartEvent)
            }
            item("Quit").action {
                view.fire(QuitEvent)
            }
        }
    }
}
