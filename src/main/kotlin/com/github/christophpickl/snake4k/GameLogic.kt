package com.github.christophpickl.snake4k

import com.github.christophpickl.snake4k.board.Direction
import com.github.christophpickl.snake4k.board.Matrix
import com.github.christophpickl.snake4k.model.Config
import com.github.christophpickl.snake4k.model.CurrentState
import com.github.christophpickl.snake4k.model.Fruit
import com.github.christophpickl.snake4k.model.Snake
import com.github.christophpickl.snake4k.view.Board
import com.github.christophpickl.snake4k.view.KeyboardWatcher
import javax.inject.Inject

class GameLogic @Inject constructor(
    private val keyboard: KeyboardWatcher,
    private val matrix: Matrix,
    private val board: Board,
    private val snake: Snake,
    private val fruit: Fruit,
    private val state: CurrentState
) {

    fun onTick(): TickResult {
        checkDirection()
        checkSnakeHit()?.let {
            return it
        }
        checkFruitEaten()
        snake.move(matrix::cellAt)
        board.repaint()
        return TickResult.Ok
    }

    fun resetState() {
        state.reset()
        keyboard.collectedDirections.clear()
        snake.head = matrix.cellAt(1, 1)
        snake.direction = Direction.RIGHT
        snake.body.clear()
        fruit.position = matrix.randomCell()
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
        state.directionsChangedCount++
        snake.direction = newDirection
    }

    private fun checkSnakeHit(): TickResult.Died? {
        val newSnakeHeadPos = snake.calculateNewHeadPosition()
        Log.debug { "New snake head position: $newSnakeHeadPos" }
        if (!matrix.cellExists(newSnakeHeadPos)) {
            return TickResult.Died("You ran into the wall.")
        }
        val newSnakeHeadCell = matrix.cellAt(newSnakeHeadPos)
        if (snake.body.contains(newSnakeHeadCell)) {
            return TickResult.Died("You ran into yourself.")
        }
        return null
    }

    private fun checkFruitEaten() {
        val newPos = snake.calculateNewHeadPosition()
        if (fruit.position.xy == newPos) {
            Log.debug { "Fruit eaten at: $newPos" }
            state.fruitsEatenCount++
            snake.growBody += Config.bodyGrowFactorOnFruitEaten
            fruit.position = board.nextFruitPosition()
        }
    }

}

sealed class TickResult {
    object Ok : TickResult()
    class Died(val message: String) : TickResult()
}
