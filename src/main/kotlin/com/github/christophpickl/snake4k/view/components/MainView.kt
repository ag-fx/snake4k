package com.github.christophpickl.snake4k.view.components

import com.github.christophpickl.snake4k.model.Settings
import com.github.christophpickl.snake4k.model.State
import com.github.christophpickl.snake4k.model.StateModel
import com.github.christophpickl.snake4k.view.KeyboardWatcher
import javafx.scene.input.KeyEvent
import tornadofx.*

class MainView : View() {

    private val board: Board by di()
    private val keyboard: KeyboardWatcher by di()
    private val state: State by di()
    private val settings: Settings by di()
    private val stateModel = StateModel(state)
    private val sizeOfBottomPanel = 40

    override val root = borderpane {
        addEventFilter(KeyEvent.KEY_PRESSED, keyboard)
        paddingAll = 0

        top {
            add(MyMenuBar(this@MainView, stateModel))
        }
        center {
            add(board)
        }
        bottom {
            hbox {
                label("Fruits eaten: ")
                label { bind(stateModel.fruitsEaten) }

                label(" - Highscore: ")
                label { bind(stateModel.highscore) }
                label(" - Sound: ")
                checkbox {
                    isSelected = state.enableSounds
                    selectedProperty().addListener { _, _, isSoundEnabled ->
                        state.enableSounds = isSoundEnabled
                        // MINOR binding didnt work :-/
//                        stateModel.enableSounds.value = isSoundEnabled
//                        stateModel.commit()
                    }
                }
            }
        }
    }

    init {
        title = "Snake4k"
        changeSize()
        settings.mapSizeProperty.addListener { _ ->
            changeSize()
        }
    }

    override fun onDock() {
        // otherwise keyboard listener won't work
        root.requestFocus()
    }

    private fun changeSize() {
        val boardSize = settings.mapSize.boardSize
        val windowSize = boardSize.width to boardSize.height + sizeOfBottomPanel
        setWindowMaxSize(windowSize.first, windowSize.second)
        setWindowMinSize(windowSize.first, windowSize.second)
    }
}
