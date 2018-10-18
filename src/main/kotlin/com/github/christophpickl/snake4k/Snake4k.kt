package com.github.christophpickl.snake4k

import com.github.christophpickl.snake4k.view.FxBridge
import com.github.christophpickl.snake4k.view.MainController
import com.github.christophpickl.snake4k.view.MainView
import com.google.inject.Guice
import javafx.application.Application
import tornadofx.*
import kotlin.reflect.KClass

object Snake4k {
    @JvmStatic
    fun main(args: Array<String>) {
        Application.launch(Snake4kFxApp::class.java, *args)
    }
}

class Snake4kFxApp : App(
    primaryView = MainView::class,
    stylesheet = InternalWindow.Styles::class
) {
    init {
        val guice = Guice.createInjector(GuiceModule())
        FX.dicontainer = object : DIContainer {
            override fun <T : Any> getInstance(type: KClass<T>) = guice.getInstance(type.java)
        }
        guice.getInstance(ApplicationManager::class.java).start()
        registerEagerSingletons()
    }

    private fun registerEagerSingletons() {
        find(MainController::class)
        find(FxBridge::class)
    }
}
