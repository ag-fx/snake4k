package com.github.christophpickl.snake4k.view.components

import com.github.christophpickl.snake4k.model.BodyGrow
import com.github.christophpickl.snake4k.model.Settings
import com.github.christophpickl.snake4k.model.SettingsModel
import com.github.christophpickl.snake4k.model.Speed
import javafx.geometry.Pos
import javafx.scene.control.Button
import javafx.scene.control.ButtonType
import javafx.scene.control.Dialog
import javafx.scene.input.KeyCode
import javafx.scene.input.KeyEvent
import javafx.scene.layout.BorderPane
import javafx.stage.StageStyle
import tornadofx.*

class SettingsView(
    settings: Settings
) : Dialog<ButtonType>() {

    private val settingsModel = SettingsModel(settings)
    private var startButton: Button by singleAssign()

    init {
        val root = BorderPane()
        with(root) {
            addEventFilter(KeyEvent.KEY_PRESSED) {
                if (it.code == KeyCode.ENTER) {
                    saveAndClose()
                }
            }
            style {
                padding = box(20.px)
            }
            center {
                form {
                    fieldset {
                        field("Speed") {
                            combobox(
                                property = settingsModel.speed,
                                values = Speed.all
                            )
                        }
                        field("Body Grow") {
                            combobox(
                                property = settingsModel.bodyGrow,
                                values = BodyGrow.all
                            )
                        }
                    }
                }
            }
            bottom {
                hbox {
                    alignment = Pos.CENTER
                    startButton = button("Start") {
                        action {
                            saveAndClose()
                        }
                    }
                }
            }
        }

        initStyle(StageStyle.UNDECORATED)
        dialogPane.content = root
        setOnShowing {
            startButton.requestFocus()
        }
    }

    private fun saveAndClose() {
        settingsModel.commit()
        result = ButtonType.CLOSE
        close()
    }

}
