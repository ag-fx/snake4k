package com.github.christophpickl.snake4k.logic

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
    private val highscore: Highscore,
    bus: EventBus
) {

    init {
        bus.register(this)

        val storageData = repo.load(settings, state)

        settings.changeBy(storageData.settings)
        state.changeBy(storageData.state)
        highscore.changeBy(storageData.highscore)
    }

    @Suppress("UNUSED_PARAMETER")
    @Subscribe
    fun onQuitEvent(event: QuitEvent) {
        repo.save(StorageData(
            settings = settings,
            state = state,
            highscore = highscore
        ))
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

    private fun Highscore.changeBy(that: Highscore) {
        this.putAll(that)
    }

}

class StorageRepository {

    private val log = KotlinLogging.logger {}
    private val prefs = Preferences.userNodeForPackage(StorageRepository::class.java)
    private val keySettingsSpeed = "settings.speed"
    private val keySettingsBodyGrow = "settings.bodyGrow"
    private val keySettingsMapSize = "settings.mapSize"
    private val keySettingsGoThroughWall = "settings.goThroughWall"
    private val keySettingsHitDelay = "settings.hitDelay"
    private val keyEnableSounds = "enableSounds"
    private val keyHighscore = "highscore"

    private val highscoreAssignment = "="
    private val highscoreSeparator = "|"

    fun save(storageData: StorageData) {
        log.debug { "save: $storageData" }
        prefs.put(keySettingsSpeed, storageData.settings.speed.storageValue)
        prefs.put(keySettingsBodyGrow, storageData.settings.bodyGrow.storageValue)
        prefs.put(keySettingsMapSize, storageData.settings.mapSize.storageValue)
        prefs.putBoolean(keySettingsGoThroughWall, storageData.settings.goThroughWall)
        prefs.putBoolean(keySettingsHitDelay, storageData.settings.hitDelay)
        prefs.putBoolean(keyEnableSounds, storageData.state.enableSounds)
        saveHighscore(storageData.highscore)
        prefs.flush()
    }

    fun load(defaultSettings: Settings, defaultState: State) = StorageData(
        settings = Settings().apply {
            speed = Speed.byStorageValue(prefs.get(keySettingsSpeed, defaultSettings.speed.storageValue))
            bodyGrow = BodyGrow.byStorageValue(prefs.get(keySettingsBodyGrow, defaultSettings.bodyGrow.storageValue))
            mapSize = MapSize.byStorageValue(prefs.get(keySettingsMapSize, defaultSettings.mapSize.storageValue))
            goThroughWall = prefs.getBoolean(keySettingsGoThroughWall, defaultSettings.goThroughWall)
            hitDelay = prefs.getBoolean(keySettingsHitDelay, defaultSettings.hitDelay)
        },
        state = State().apply {
            enableSounds = prefs.getBoolean(keyEnableSounds, defaultState.enableSounds)
        },
        highscore = Highscore().apply {
            loadHighscore().forEach { loaded ->
                this[loaded.first] = loaded.second
            }
        }
    ).also {
        log.debug { "Loaded: $it" }
    }

    private fun saveHighscore(highscore: Highscore) {
        val highscoreAsString = highscore.get()
            .map { "${it.key.toPrefs()}$highscoreAssignment${it.value}" }
            .joinToString(highscoreSeparator)
        prefs.put(keyHighscore, highscoreAsString)
    }

    private fun loadHighscore(): List<Pair<HighscoreSettings, Int>> {
        val rawData = prefs.get(keyHighscore, "")
        if (rawData.isEmpty()) return emptyList()
        return rawData.split(highscoreSeparator).map {
            val parts = it.split(highscoreAssignment)
            Pair(parseHighscoreSettings(parts[0]), parts[1].toInt())
        }
    }

    private fun parseHighscoreSettings(data: String) = HighscoreSettings(
        speed = Speed.byStorageValue(data.elementAt(0).toString()),
        bodyGrow = BodyGrow.byStorageValue(data.elementAt(1).toString()),
        mapSize = MapSize.byStorageValue(data.elementAt(2).toString()),
        goThroughWall = data.elementAt(3) == 'T',
        hitDelay = data.elementAt(4) == 'T'
    )

    private fun HighscoreSettings.toPrefs() =
        speed.storageValue +
            bodyGrow.storageValue +
            mapSize.storageValue +
            goThroughWall.storageValue +
            hitDelay.storageValue

    private val Boolean.storageValue get() = if (this) "T" else "F"
}

data class StorageData(
    val settings: Settings,
    val state: State,
    val highscore: Highscore
)

private val Speed.storageValue
    get() = when (this) {
        Speed.Slow -> "S"
        Speed.Normal -> "N"
        Speed.Fast -> "F"
    }

private fun Speed.Companion.byStorageValue(value: String) = all.first { it.storageValue == value }

private val BodyGrow.storageValue
    get() = when (this) {
        BodyGrow.Little -> "L"
        BodyGrow.Normal -> "N"
        BodyGrow.Enormous -> "E"
    }

private fun BodyGrow.Companion.byStorageValue(value: String) = all.first { it.storageValue == value }

private val MapSize.storageValue
    get() = when (this) {
        MapSize.Tiny -> "T"
        MapSize.Normal -> "N"
        MapSize.Huge -> "H"
    }

private fun MapSize.Companion.byStorageValue(value: String) = all.first { it.storageValue == value }
