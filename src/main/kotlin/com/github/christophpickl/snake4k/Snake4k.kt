package com.github.christophpickl.snake4k

import javafx.application.Application
import mu.KotlinLogging.logger

object Snake4k {

    init {
        configureLogging()
    }

    private val log = logger {}

    @JvmStatic
    fun main(args: Array<String>) {
        log.info { "Application start" }
        Application.launch(Snake4kFxApp::class.java, *args)
    }
}
