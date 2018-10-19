package com.github.christophpickl.snake4k

import tornadofx.*

object ApplicationReadyEvent : FXEvent()

class RestartGuiceEvent(
    var wasDispatchedInFx: Boolean = false
)

class RestartFxEvent(
    var wasDispatchedInGuice: Boolean = false
) : FXEvent()

class GameOverEvent(
    val detailMessage: String,
    val fruitsEaten: Int,
    val secondsPlayed: Int
) : FXEvent()

object PauseEvent : FXEvent()

object QuitEvent : FXEvent()

class ExceptionEvent(
    val exception: Exception
) : FXEvent()

object FruitEatenEvent
