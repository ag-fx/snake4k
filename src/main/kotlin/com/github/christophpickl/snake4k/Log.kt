package com.github.christophpickl.snake4k

import com.github.christophpickl.snake4k.model.Config

object Log {
    inline fun debug(message: () -> String) {
        @Suppress("ConstantConditionIf")
        if (Config.logEnabled) {
            println("[LOG] ${message()}")
        }
    }
}
