package com.github.christophpickl.snake4k.view

import javafx.scene.control.MenuBar
import javafx.scene.input.KeyCode
import javafx.scene.input.KeyCodeCombination
import javafx.scene.input.KeyCombination
import tornadofx.*

class MainView : View() {
    override val root = borderpane {
        top {
            add(MyMenuBar(this@MainView))
        }
        bottom {
            label("Haha")
        }
    }
}

class MainController : Controller() {

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
