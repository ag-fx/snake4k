package com.github.christophpickl.snake4k.logic

import com.github.christophpickl.snake4k.FruitEatenEvent
import com.github.christophpickl.snake4k.board.Cell
import com.github.christophpickl.snake4k.board.Direction
import com.github.christophpickl.snake4k.board.Matrix
import com.github.christophpickl.snake4k.model.Fruit
import com.github.christophpickl.snake4k.model.Settings
import com.github.christophpickl.snake4k.model.Snake
import com.github.christophpickl.snake4k.model.State
import com.github.christophpickl.snake4k.view.KeyboardWatcher
import com.google.common.eventbus.EventBus
import javafx.application.Platform
import mu.KotlinLogging
import javax.inject.Inject

class GameLogic @Inject constructor(
    private val keyboard: KeyboardWatcher,
    private val matrix: Matrix,
    private val snake: Snake,
    private val fruit: Fruit,
    private val state: State,
    private val bus: EventBus,
    private val settings: Settings
) {
    private val log = KotlinLogging.logger {}

    fun onTick(): TickResult {
        checkDirection()
        checkSnakeHit()?.let {
            return it
        }
        checkFruitEaten()
        snake.move {
            matrix.cellAt(calculateNewHeadPosition())
        }
        return TickResult.Ok
    }

    fun resetState() {
        Platform.runLater { state.reset() }
        keyboard.collectedDirections.clear()
        snake.head = matrix.cellAt(1, 1)
        snake.direction = Direction.Right
        snake.body.clear()
        snake.body.add(matrix.cellAt(0, 1))
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
        state.directionsChanged++
        snake.direction = newDirection
    }

    private fun checkSnakeHit(): TickResult.Died? {
        val newSnakeHeadPos = calculateNewHeadPosition()
        log.trace { "New snake head position: $newSnakeHeadPos" }
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
        val newPos = calculateNewHeadPosition()
        if (fruit.position.xy == newPos) {
            log.debug { "Fruit eaten at: $newPos" }
            Platform.runLater { state.fruitsEaten++ }
            snake.growBody += settings.bodyGrow.factor
            fruit.position = nextFruitPosition()
            bus.post(FruitEatenEvent)
        }
    }

    private fun nextFruitPosition(): Cell =
        generateSequence {
            matrix.randomCell()
        }.first {
            !snake.contains(it) && fruit.position != it
        }.apply {
            log.debug { "Next fruit position: $this" }
        }

    private fun calculateNewHeadPosition(): Pair<Int, Int> {
        val newPos = Pair(
            snake.head.x + snake.direction.coordinatesChange.first,
            snake.head.y + snake.direction.coordinatesChange.second
        )
        if (!settings.goThroughWall || matrix.cellExists(newPos)) {
            return newPos
        }
        return when (snake.direction) {
            Direction.Up -> Pair(newPos.first, settings.mapSize.rows - 1)
            Direction.Down -> Pair(newPos.first, 0)
            Direction.Left -> Pair(settings.mapSize.cols - 1, newPos.second)
            Direction.Right -> Pair(0, newPos.second)
        }
    }

}

sealed class TickResult {
    object Ok : TickResult()
    class Died(val message: String) : TickResult()
}
