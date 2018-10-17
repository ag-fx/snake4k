package com.github.christophpickl.snake4k

import com.github.christophpickl.snake4k.board.Board
import com.github.christophpickl.snake4k.board.Cell
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
    private val board: Board,
    private val window: Window,
    private val snake: Snake,
    private val fruit: Fruit,
    private val keyboard: KeyboardWatcher,
    private val onQuit: () -> Unit
) {

    private var timer: Timer? = null
    private var timeStarted = LocalDateTime.now()
    private var fruitsEatenCount = 0
    private var directionsChangedCount = 0

    fun restart() {
        resetState()
        timer = Timer(true).also { currentTimer ->
            currentTimer.scheduleAtFixedRate(GameTimerTask({
                onTick()
            }, { e ->
                e.printStackTrace()
                currentTimer.cancel()
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
        timeStarted = LocalDateTime.now()
        fruitsEatenCount = 0
        directionsChangedCount = 0
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

    private fun onTick() {
        checkDirection()
        if (checkSnakeHit()) {
            return
        }
        checkFruitEaten()
        moveSnake()
        board.repaint()
    }

    private fun checkDirection() {
        if (keyboard.collectedDirections.isEmpty()) {
            return
        }
        val newDirection = keyboard.collectedDirections.removeFirst()
        if (!newDirection.isNextTo(snake.direction)) {
            checkDirection()
            return
        }
        if (snake.direction == newDirection) {
            checkDirection()
            return
        }
        directionsChangedCount++
        snake.direction = newDirection
    }

    private fun checkSnakeHit(): Boolean {
        val newSnakeHeadPos = snake.calculateNewHeadPosition()
        Log.debug { "New snake head position will be: $newSnakeHeadPos" }
        if (!matrix.cellExists(newSnakeHeadPos)) {
            gameOver("You ran into the wall.")
            return true
        }
        val newSnakeHeadCell = matrix.cellAt(newSnakeHeadPos)
        if (snake.body.contains(newSnakeHeadCell)) {
            gameOver("You ran into yourself.")
            return true
        }
        return false
    }

    private fun gameOver(detailMessage: String) {
        timer!!.cancel()
        val secondsPlayed = Duration.between(timeStarted, LocalDateTime.now()).seconds
        val result = JOptionPane.showOptionDialog(
            window,
            "$detailMessage\n" +
                "Fruits eaten: $fruitsEatenCount\n" +
                "Time survived: ${formatTime(secondsPlayed)}\n" +
                "Direction changes needed: $directionsChangedCount"
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
        "${if (seconds >= 60) "${seconds / 60}min " else "" }${seconds % 60}sec"

    private fun checkFruitEaten() {
        val newPos = snake.calculateNewHeadPosition()
        if (fruit.position.xy == newPos) {
            Log.debug { "Fruit eaten at: $newPos" }
            fruitsEatenCount++
            snake.growBody += Config.bodyGrowFactorOnFruitEaten
            fruit.position = nextFruitPosition()
        }
    }

    private fun nextFruitPosition(): Cell {
        var newPosition = matrix.randomCell()
        while (snake.contains(newPosition) || fruit.position == newPosition) {
            newPosition = matrix.randomCell()
        }
        return newPosition
    }

    private fun moveSnake() {
        if (snake.growBody == 0) {
            snake.removeLast()
        } else {
            snake.growBody--
        }

        val newPos = snake.calculateNewHeadPosition()
        snake.body.add(0, snake.head)
        val newHead = matrix.cellAt(newPos)
        snake.head = newHead
    }

}
