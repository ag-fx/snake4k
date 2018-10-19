package com.github.christophpickl.snake4k.model

import javafx.beans.property.SimpleIntegerProperty
import javafx.beans.property.SimpleObjectProperty
import tornadofx.*
import java.time.LocalDateTime

class State {
    val fruitsEatenProperty = SimpleIntegerProperty(this, "fruitsEaten", 0)
    var fruitsEaten by fruitsEatenProperty

    val highscoreProperty = SimpleIntegerProperty(this, "highscore", -1)
    var highscore by highscoreProperty

    var directionsChanged = 0
    var timeStarted = LocalDateTime.now()!!

    val gameStateProperty = SimpleObjectProperty(this, "gameState", GameState.NotRunning)
    var gameState by gameStateProperty

    fun reset() {
        fruitsEaten = 0
        directionsChanged = 0
        timeStarted = LocalDateTime.now()
    }

}

class StateModel(state: State) : ViewModel() {
    val gameState = bind(propertyProducer = state::gameStateProperty)
    val fruitsEaten = bind(propertyProducer = state::fruitsEatenProperty)
    val highscore = bind(propertyProducer = state::highscoreProperty)
}

enum class GameState {
    NotRunning, Running, Paused;
}
