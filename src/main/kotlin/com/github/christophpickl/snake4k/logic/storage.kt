package com.github.christophpickl.snake4k.logic

import com.github.christophpickl.snake4k.GameOverEvent
import com.github.christophpickl.snake4k.QuitEvent
import com.github.christophpickl.snake4k.model.BodyGrow
import com.github.christophpickl.snake4k.model.MapSize
import com.github.christophpickl.snake4k.model.Settings
import com.github.christophpickl.snake4k.model.Speed
import com.github.christophpickl.snake4k.model.State
import com.google.common.eventbus.EventBus
import com.google.common.eventbus.Subscribe
import mu.KotlinLogging
import java.util.prefs.Preferences
import javax.inject.Inject

class StorageService @Inject constructor(
    private val repo: StorageRepository,
    private val settings: Settings,
    private val state: State,
    bus: EventBus
) {

    private var currentHighscore: Int

    init {
        bus.register(this)

        val storageData = repo.load(settings, state)

        settings.changeBy(storageData.settings)
        state.changeBy(storageData.state)
        currentHighscore = storageData.highscore
    }

    @Suppress("UNUSED_PARAMETER")
    @Subscribe
    fun onQuitEvent(event: QuitEvent) {
        repo.save(StorageData(
            highscore = currentHighscore,
            settings = settings,
            state = state
        ))
    }

    @Subscribe
    fun onGameOverEvent(event: GameOverEvent) {
        if (event.fruitsEaten > currentHighscore) {
            currentHighscore = event.fruitsEaten
        }
    }

}

private fun Settings.changeBy(that: Settings) {
    this.speed = that.speed
    this.bodyGrow = that.bodyGrow
    this.mapSize = that.mapSize
    this.goThroughWall = that.goThroughWall
    this.hitDelay = that.hitDelay
}

private fun State.changeBy(that: State) {
    this.enableSounds = that.enableSounds
    this.highscore = that.highscore
}

class StorageRepository {

    private val log = KotlinLogging.logger {}
    private val prefs = Preferences.userNodeForPackage(StorageRepository::class.java)
    private val keyHighscore = "highscore"
    private val keySettingsSpeed = "settings.speed"
    private val keySettingsBodyGrow = "settings.bodyGrow"
    private val keySettingsMapSize = "settings.mapSize"
    private val keySettingsGoThroughWall = "settings.goThroughWall"
    private val keySettingsHitDelay = "settings.hitDelay"
    private val keyEnableSounds = "enableSounds"

    fun save(storageData: StorageData) {
        log.debug { "save: $storageData" }
        prefs.putInt(keyHighscore, storageData.highscore)
        prefs.put(keySettingsSpeed, storageData.settings.speed.storageValue)
        prefs.put(keySettingsBodyGrow, storageData.settings.bodyGrow.storageValue)
        prefs.put(keySettingsMapSize, storageData.settings.mapSize.storageValue)
        prefs.putBoolean(keySettingsGoThroughWall, storageData.settings.goThroughWall)
        prefs.putBoolean(keySettingsHitDelay, storageData.settings.hitDelay)
        prefs.putBoolean(keyEnableSounds, storageData.state.enableSounds)
        prefs.flush()
    }

    fun load(defaultSettings: Settings, defaultState: State) = StorageData(
        highscore = prefs.getInt(keyHighscore, 0),
        settings = Settings().apply {
            speed = Speed.byStorageValue(prefs.get(keySettingsSpeed, defaultSettings.speed.storageValue))
            bodyGrow = BodyGrow.byStorageValue(prefs.get(keySettingsBodyGrow, defaultSettings.bodyGrow.storageValue))
            mapSize = MapSize.byStorageValue(prefs.get(keySettingsMapSize, defaultSettings.mapSize.storageValue))
            goThroughWall = prefs.getBoolean(keySettingsGoThroughWall, defaultSettings.goThroughWall)
            hitDelay = prefs.getBoolean(keySettingsHitDelay, defaultSettings.hitDelay)
        },
        state = State().apply {
            enableSounds = prefs.getBoolean(keyEnableSounds, defaultState.enableSounds)
        }
    ).also {
        log.debug { "Loaded: $it" }
    }

}

data class StorageData(
    val highscore: Int,
    val settings: Settings,
    val state: State
)

private val Speed.storageValue
    get() = when (this) {
        Speed.Slow -> "slow"
        Speed.Normal -> "normal"
        Speed.Fast -> "fast"
    }

private fun Speed.Companion.byStorageValue(value: String) = all.first { it.storageValue == value }

private val BodyGrow.storageValue
    get() = when (this) {
        BodyGrow.Little -> "little"
        BodyGrow.Normal -> "normal"
        BodyGrow.Enormous -> "enormous"
    }

private fun BodyGrow.Companion.byStorageValue(value: String) = all.first { it.storageValue == value }

private val MapSize.storageValue
    get() = when (this) {
        MapSize.Tiny -> "tiny"
        MapSize.Normal -> "normal"
        MapSize.Huge -> "huge"
    }

private fun MapSize.Companion.byStorageValue(value: String) = all.first { it.storageValue == value }
