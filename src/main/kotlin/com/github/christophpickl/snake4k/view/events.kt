package com.github.christophpickl.snake4k.view

import tornadofx.*

object RestartEvent : FXEvent()

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
