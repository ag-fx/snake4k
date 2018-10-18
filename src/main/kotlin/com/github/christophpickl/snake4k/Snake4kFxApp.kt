package com.github.christophpickl.snake4k

import com.github.christophpickl.snake4k.view.FxBridge
import com.github.christophpickl.snake4k.view.MainController
import com.github.christophpickl.snake4k.view.MainView
import com.google.inject.Guice
import javafx.stage.Stage
import tornadofx.*
import kotlin.reflect.KClass

class Snake4kFxApp : App(
    primaryView = MainView::class,
    stylesheet = InternalWindow.Styles::class
) {
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
        println("start()")
        super.start(stage)
        //    defaultCloseOperation = WindowConstants.DO_NOTHING_ON_CLOSE
        //    setLocationRelativeTo(null)
        stage.setOnCloseRequest {
            println("stage.setOnCloseRequest {}")
        }

        appManager.start()
    }

    private fun registerEagerSingletons() {
        find(MainController::class)
        find(FxBridge::class)
    }
}
