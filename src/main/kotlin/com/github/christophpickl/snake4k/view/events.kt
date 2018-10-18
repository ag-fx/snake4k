package com.github.christophpickl.snake4k.view

import tornadofx.*

object RestartEvent : FXEvent()
object QuitEvent : FXEvent()
class GameOverEvent(
    val detailMessage: String,
    val fruitsEaten: Int,
    val secondsPlayed: Int
) : FXEvent()
