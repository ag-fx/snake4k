package com.github.christophpickl.snake4k

object Log {
    inline fun debug(message: () -> String) {
        @Suppress("ConstantConditionIf")
        if (Config.logEnabled) {
            println("[LOG] ${message()}")
        }
    }
}
