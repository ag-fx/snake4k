package com.github.christophpickl.snake4k.view.components

import com.github.christophpickl.snake4k.board.Cell
import com.github.christophpickl.snake4k.board.Direction
import com.github.christophpickl.snake4k.board.Matrix
import com.github.christophpickl.snake4k.model.Config
import com.github.christophpickl.snake4k.model.Fruit
import com.github.christophpickl.snake4k.model.Snake
import com.github.christophpickl.snake4k.view.cellStrokeLines
import com.github.christophpickl.snake4k.view.cellStrokeLinesBy
import javafx.scene.canvas.Canvas
import javafx.scene.canvas.GraphicsContext
import javafx.scene.paint.Color
import mu.KotlinLogging
import javax.inject.Inject

class Board @Inject constructor(
    private val matrix: Matrix,
    private val snake: Snake,
    private val fruit: Fruit
) : Canvas(
    Config.boardSize.width.toDouble(),
    Config.boardSize.height.toDouble()
) {
    private val log = KotlinLogging.logger {}

    init {
        repaint()
    }

    fun repaint() {
        val g = graphicsContext2D
        g.stroke = Color.BLACK

        matrix.forEach { cell ->
            val rectX = cell.x * Config.cellSizeAsDouble
            val rectY = cell.y * Config.cellSizeAsDouble

            g.fill = cell.fillColor
            g.fillRect(rectX, rectY, Config.cellSizeAsDouble, Config.cellSizeAsDouble)

            if (cell.isOwnedBySnake()) {
                g.drawSnakeLines(cell, rectX, rectY)
            } else if (cell.isOwnedByFruit()) {
                g.cellStrokeLines(rectX, rectY, Direction.all)
            }
        }
    }

    private fun Cell.isOwnedBySnake() = snake.body.contains(this) || snake.head == this
    private fun Cell.isOwnedByFruit() = this == fruit.position

    private val Cell.fillColor
        get() =
            when {
                snake.body.contains(this) -> Config.snakeBodyColor
                snake.head == this -> Config.snakeHeadColor
                isOwnedByFruit() -> Config.fruitColor
                else -> Config.boardColor
            }

    private fun GraphicsContext.drawSnakeLines(cell: Cell, x: Double, y: Double) {
        val (neighbour1, neighbour2) = if (cell == snake.head) {
            snake.body.firstOrNull() to null
        } else {
            if (cell == snake.body.last()) {
                (snake.body.elementAtOrNull(snake.body.size - 2) ?: snake.head) to null
            } else {
                val cellIndex = snake.body.indexOf(cell)
                (if (cellIndex == 0) snake.head else snake.body[cellIndex - 1]) to snake.body[cellIndex + 1]
            }
        }
        cellStrokeLinesBy(cell, neighbour1, neighbour2, x, y)
    }

}
