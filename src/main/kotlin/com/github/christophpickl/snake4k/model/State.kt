package com.github.christophpickl.snake4k.model

import javafx.beans.property.SimpleBooleanProperty
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

    val enableSoundsProperty = SimpleBooleanProperty(this, "enableSounds", true)
    var enableSounds by enableSoundsProperty

    fun reset() {
        fruitsEaten = 0
        directionsChanged = 0
        timeStarted = LocalDateTime.now()
    }

}

class StateModel(val state: State) : ViewModel() {
    val gameState = bind(propertyProducer = state::gameStateProperty)
    val fruitsEaten = bind(propertyProducer = state::fruitsEatenProperty)
    val highscore = bind(propertyProducer = state::highscoreProperty)
    val enableSounds = bind(propertyProducer = state::enableSoundsProperty)
}

enum class GameState {
    NotRunning, Running, Paused;
}
