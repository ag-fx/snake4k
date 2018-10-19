package com.github.christophpickl.snake4k

import com.github.christophpickl.snake4k.logic.configureLogging
import com.github.christophpickl.snake4k.view.Snake4kFxApp
import javafx.application.Application
import mu.KotlinLogging.logger

object Snake4k {

    @JvmStatic
    fun main(args: Array<String>) {
        configureLogging()
        logger {}.info { "Application start" }
        Application.launch(Snake4kFxApp::class.java, *args)
    }

}
