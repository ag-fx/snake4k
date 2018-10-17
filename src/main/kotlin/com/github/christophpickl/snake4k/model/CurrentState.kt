package com.github.christophpickl.snake4k.model

import java.time.LocalDateTime

class CurrentState {
    var fruitsEatenCount = 0
    var directionsChangedCount = 0
    var timeStarted = LocalDateTime.now()!!

    fun reset() {
        fruitsEatenCount = 0
        directionsChangedCount = 0
        timeStarted = LocalDateTime.now()
    }

}
