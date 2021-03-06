package com.github.christophpickl.snake4k.logic

import com.github.christophpickl.snake4k.FruitEatenEvent
import com.github.christophpickl.snake4k.GameOverEvent
import com.github.christophpickl.snake4k.model.State
import com.google.common.eventbus.EventBus
import com.google.common.eventbus.Subscribe
import javafx.scene.media.Media
import javafx.scene.media.MediaPlayer
import javafx.util.Duration
import javax.inject.Inject

class SoundService @Inject constructor(
    state: State,
    bus: EventBus
) {
    private lateinit var soundPlayer: SoundPlayer

    init {
        bus.register(this)
        changePlayer(state.enableSounds)
        state.enableSoundsProperty.addListener { _, _, isSoundEnabled ->
            changePlayer(isSoundEnabled)
        }
    }

    @Suppress("UNUSED_PARAMETER")
    @Subscribe
    fun onFruitEatenEvent(event: FruitEatenEvent) {
        soundPlayer.play(Sound.EatFruit)
    }

    @Suppress("UNUSED_PARAMETER")
    @Subscribe
    fun onGameOverEvent(event: GameOverEvent) {
        soundPlayer.play(Sound.GameOver)
    }

    private fun changePlayer(isSoundEnabled: Boolean) {
        soundPlayer = if (isSoundEnabled) EnabledSoundPlayer else DisabledSoundPlayer
    }
}

private interface SoundPlayer {
    fun play(sound: Sound)
}

private object DisabledSoundPlayer : SoundPlayer {
    override fun play(sound: Sound) {}
}

private object EnabledSoundPlayer : SoundPlayer {
    override fun play(sound: Sound) {
        sound.play()
    }
}

private enum class Sound(file: String) {
    EatFruit("eat_fruit.wav"),
    GameOver("game_over.wav");

    private val player = MediaPlayer(Media(javaClass.getResource("/sounds/$file").toURI().toString()))

    init {
        player.setOnEndOfMedia {
            player.seek(Duration.ZERO)
            player.pause()
        }
    }

    fun play() {
        if (player.status == javafx.scene.media.MediaPlayer.Status.PLAYING) {
            player.seek(Duration.ZERO)
        } else {
            player.play()
        }
    }
}
