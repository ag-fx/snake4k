package com.github.christophpickl.snake4k.view

import com.github.christophpickl.snake4k.logic.ApplicationManager
import com.github.christophpickl.snake4k.logic.GuiceModule
import com.github.christophpickl.snake4k.view.components.MainView
import com.google.inject.Guice
import javafx.stage.Stage
import mu.KotlinLogging
import tornadofx.*
import kotlin.reflect.KClass

class Snake4kFxApp : App(
    primaryView = MainView::class,
    stylesheet = InternalWindow.Styles::class
) {

    private val log = KotlinLogging.logger {}
    private val appManager: ApplicationManager

    init {
        val guice = Guice.createInjector(GuiceModule())
        FX.dicontainer = object : DIContainer {
            override fun <T : Any> getInstance(type: KClass<T>) = guice.getInstance(type.java)
        }
        appManager = guice.getInstance(ApplicationManager::class.java)
        registerEagerSingletons()
    }

    override fun start(stage: Stage) {
        log.debug { "Start UI" }
        super.start(stage)

        stage.setOnCloseRequest {
            it.consume() // disable macos' default CMD+Q
            fire(QuitEvent)
        }

        fire(ApplicationReadyEvent)
    }

    private fun registerEagerSingletons() {
        find(MainController::class)
        find(SettingsController::class)
        find(FxBridge::class)
    }
}
