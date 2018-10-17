package com.github.christophpickl.snake4k

import com.google.inject.Guice

object Snake4k {

    @JvmStatic
    fun main(args: Array<String>) {
        val guice = Guice.createInjector(GuiceModule())
        val game = guice.getInstance(GameManager::class.java)
        game.start()
    }
}

