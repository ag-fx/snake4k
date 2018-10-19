package com.github.christophpickl.snake4k.logic

import com.github.christophpickl.snake4k.model.Config
import com.github.christophpickl.snake4k.model.GameState
import com.github.christophpickl.snake4k.model.State
import com.github.christophpickl.snake4k.view.ExceptionEvent
import com.github.christophpickl.snake4k.view.GameOverEvent
import com.google.common.eventbus.EventBus
import javafx.application.Platform
import mu.KotlinLogging
import java.awt.EventQueue
import java.time.Duration
import java.time.LocalDateTime
import java.util.Timer
import java.util.TimerTask
import javax.inject.Inject

class GameEngine @Inject constructor(
    private val logic: GameLogic,
    private val state: State,
    private val bus: EventBus
) {

    private val log = KotlinLogging.logger {}
    private var timer: Timer? = null

    fun restart() {
        logic.resetState()
        stop()

        Platform.runLater { state.gameState = GameState.Running }
        timer = Timer(true).also { currentTimer ->
            currentTimer.scheduleAtFixedRate(GameTimerTask(state, {
                val result = logic.onTick()
                if (result is TickResult.Died) {
                    gameOver(result.message)
                }
            }, { e ->
                currentTimer.cancel()
                e.printStackTrace()
                bus.post(ExceptionEvent(e))
            }), 0L, Config.tickTimeMs)
        }
    }

    fun stop() {
        log.info { "stop engine" }
        gameEnded()
    }

    private class GameTimerTask(
        private val state: State,
        private val onTick: () -> Unit,
        private val onException: (Exception) -> Unit
    ) : TimerTask() {
        override fun run() {
            // TODO do calculations on Timer thread, and only board.repaint on UI thread
            if (state.gameState != GameState.Running) {
                return
            }
            if (!EventQueue.isDispatchThread()) {
                EventQueue.invokeLater(this)
            } else {
                try {
                    onTick()
                } catch (e: Exception) {
                    onException(e)
                }
            }
        }
    }

    private fun gameOver(detailMessage: String) {
        log.info { "Game over: $detailMessage" }
        gameEnded()
        val secondsPlayed = Duration.between(state.timeStarted, LocalDateTime.now()).seconds

        bus.post(GameOverEvent(
            detailMessage = detailMessage,
            fruitsEaten = state.fruitsEaten,
            secondsPlayed = secondsPlayed.toInt()
        ))
    }

    private fun gameEnded() {
        Platform.runLater { state.gameState = GameState.NotRunning }
        timer?.cancel()
    }

}
