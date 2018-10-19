package com.github.christophpickl.snake4k.view

import com.github.christophpickl.snake4k.logic.ApplicationManager
import com.github.christophpickl.snake4k.model.Settings
import com.github.christophpickl.snake4k.view.components.SettingsView
import javafx.application.Platform
import mu.KotlinLogging
import tornadofx.*

class SettingsController : Controller() {

    private val logg = KotlinLogging.logger {}
    private val appManager: ApplicationManager by di()
    private val settings: Settings by di()
    private val settingsView = SettingsView(settings)

    init {
        subscribe<ApplicationReadyEvent> {
            showAndRun()
        }
        subscribe<RestartFxEvent> {
            showAndRun()
        }
        subscribe<QuitEvent> {
            settingsView.close()
        }
        settingsView.setOnCloseRequest {
            appManager.start()
        }
    }

    private fun showAndRun() {
        logg.debug { "show settings" }
        Platform.runLater {
            settingsView.show()
        }
    }

}
