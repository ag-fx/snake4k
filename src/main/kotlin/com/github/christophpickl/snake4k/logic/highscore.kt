package com.github.christophpickl.snake4k.logic

import com.github.christophpickl.snake4k.GameOverEvent
import com.github.christophpickl.snake4k.GameStartEvent
import com.github.christophpickl.snake4k.model.BodyGrow
import com.github.christophpickl.snake4k.model.MapSize
import com.github.christophpickl.snake4k.model.Settings
import com.github.christophpickl.snake4k.model.Speed
import com.github.christophpickl.snake4k.model.State
import com.google.common.eventbus.EventBus
import com.google.common.eventbus.Subscribe
import javafx.application.Platform
import javax.inject.Inject

class HighscoreService @Inject constructor(
    private val highscore: Highscore,
    private val settings: Settings,
    private val state: State,
    bus: EventBus
) {

    init {
        bus.register(this)
    }

    @Subscribe
    fun onGameOverEvent(event: GameOverEvent) {
        val highscoreSettings = settings.toHighscoreSettings()
        val currentHighscore = highscore[highscoreSettings]
        if (event.fruitsEaten > currentHighscore) {
            highscore[highscoreSettings] = event.fruitsEaten
        }
    }

    @Suppress("UNUSED_PARAMETER")
    @Subscribe
    fun onGameStartEvent(event: GameStartEvent) {
        Platform.runLater {
            state.highscore = highscore[settings.toHighscoreSettings()]
        }
    }

}

fun Settings.toHighscoreSettings() = HighscoreSettings(
    speed = speed,
    bodyGrow = bodyGrow,
    mapSize = mapSize,
    goThroughWall = goThroughWall,
    hitDelay = hitDelay
)

class Highscore {

    private val data = mutableMapOf<HighscoreSettings, Int>()

    fun get(): Map<HighscoreSettings, Int> = data

    fun putAll(other: Highscore) {
        data.putAll(other.data)
    }

    operator fun get(settings: HighscoreSettings): Int =
        data[settings] ?: 0

    operator fun set(settings: HighscoreSettings, value: Int) {
        data[settings] = value
    }

    override fun toString() = "Highscore(data=$data)"
}

data class HighscoreSettings(
    val speed: Speed,
    val bodyGrow: BodyGrow,
    val mapSize: MapSize,
    val goThroughWall: Boolean,
    val hitDelay: Boolean
)
