package com.github.christophpickl.snake4k

import com.github.christophpickl.snake4k.board.Direction
import com.github.christophpickl.snake4k.board.Matrix
import com.github.christophpickl.snake4k.model.Fruit
import com.github.christophpickl.snake4k.model.Snake
import com.github.christophpickl.snake4k.view.KeyboardWatcher
import com.github.christophpickl.snake4k.view.Window
import java.awt.EventQueue
import java.time.Duration
import java.time.LocalDateTime
import java.util.Timer
import java.util.TimerTask
import javax.swing.JOptionPane

class GameEngine(
    private val matrix: Matrix,
    private val window: Window,
    private val snake: Snake,
    private val fruit: Fruit,
    private val keyboard: KeyboardWatcher,
    private val onQuit: () -> Unit,
    private val logic: GameLogic,
    private val state: CurrentState
) {

    private var timer: Timer? = null

    fun restart() {
        resetState()
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

    private fun resetState() {
        stop()
        state.reset()
        keyboard.collectedDirections.clear()
        snake.head = matrix.cellAt(1, 1)
        snake.direction = Direction.RIGHT
        snake.body.clear()
        fruit.position = matrix.randomCell()
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
            0 -> onQuit()
            1 -> restart()
        }
    }

    private fun formatTime(seconds: Long) =
        "${if (seconds >= 60) "${seconds / 60}min " else ""}${seconds % 60}sec"

}
