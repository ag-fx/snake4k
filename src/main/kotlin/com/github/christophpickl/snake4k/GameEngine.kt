package com.github.christophpickl.snake4k

import com.github.christophpickl.snake4k.model.Config
import com.github.christophpickl.snake4k.model.CurrentState
import com.github.christophpickl.snake4k.view.Window
import com.google.common.eventbus.EventBus
import java.awt.EventQueue
import java.time.Duration
import java.time.LocalDateTime
import java.util.Timer
import java.util.TimerTask
import javax.inject.Inject
import javax.swing.JOptionPane

class GameEngine @Inject constructor(
    private val window: Window,
    private val logic: GameLogic,
    private val state: CurrentState,
    private val bus: EventBus
) {

    private var timer: Timer? = null

    fun restart() {
        logic.resetState()
        stop()

        timer = Timer(true).also { currentTimer ->
            currentTimer.scheduleAtFixedRate(GameTimerTask({
                val result = logic.onTick()
                if (result is TickResult.Died) {
                    gameOver(result.message)
                }
            }, { e ->
                currentTimer.cancel()
                e.printStackTrace()
                JOptionPane.showMessageDialog(
                    window, "${e.javaClass.simpleName}: ${e.message}", "Exception thrown!", JOptionPane.ERROR_MESSAGE
                )
            }), 0L, Config.tickTimeMs)
        }
    }

    fun stop() {
        timer?.cancel()
    }

    private class GameTimerTask(
        private val onTick: () -> Unit,
        private val onException: (Exception) -> Unit
    ) : TimerTask() {
        override fun run() {
            // TODO do calculations on Timer thread, and only board.repaint on UI thread
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
        Log.debug { "Game over: $detailMessage" }
        timer!!.cancel()
        val secondsPlayed = Duration.between(state.timeStarted, LocalDateTime.now()).seconds
        val result = JOptionPane.showOptionDialog(
            window,
            "$detailMessage\n" +
                "Fruits eaten: ${state.fruitsEatenCount}\n" +
                "Time survived: ${formatTime(secondsPlayed)}"
            , "Game over!",
            JOptionPane.DEFAULT_OPTION, JOptionPane.WARNING_MESSAGE, null,
            arrayOf("Quit", "Restart"), "Restart"
        )
        when (result) {
            0 -> bus.post(QuitEvent)
            1 -> bus.post(RestartEvent)
        }
    }

    private fun formatTime(seconds: Long) =
        "${if (seconds >= 60) "${seconds / 60}min " else ""}${seconds % 60}sec"

}
