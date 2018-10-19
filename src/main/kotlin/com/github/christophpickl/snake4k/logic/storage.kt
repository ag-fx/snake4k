package com.github.christophpickl.snake4k.logic

import com.github.christophpickl.snake4k.GameOverEvent
import com.github.christophpickl.snake4k.model.State
import com.google.common.eventbus.EventBus
import com.google.common.eventbus.Subscribe
import mu.KotlinLogging
import java.util.prefs.Preferences
import javax.inject.Inject

class StorageService @Inject constructor(
    private val repo: StorageRepository,
    state: State,
    bus: EventBus
) {

    private var storedHighscore: Int

    init {
        bus.register(this)

        val storageData = repo.load()
        state.highscore = storageData.highscore
        storedHighscore = storageData.highscore
    }

    @Subscribe
    fun onGameOverEvent(event: GameOverEvent) {
        if (event.fruitsEaten > storedHighscore) {
            repo.save(StorageData(
                highscore = event.fruitsEaten
            ))
        }
    }

}

class StorageRepository {

    private val log = KotlinLogging.logger {}
    private val prefs = Preferences.userNodeForPackage(StorageRepository::class.java)
    private val keyHighscore = "highscore"

    fun save(storageData: StorageData) {
        log.debug { "save: $storageData" }
        prefs.putInt(keyHighscore, storageData.highscore)
        prefs.flush()
    }

    fun load(): StorageData {
        return StorageData(
            highscore = prefs.getInt(keyHighscore, 0)
        ).also {
            log.debug { "Loaded: $it" }
        }
    }
}

data class StorageData(
    val highscore: Int
)
