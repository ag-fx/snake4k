package com.github.christophpickl.snake4k.view

import com.github.christophpickl.snake4k.board.Cell
import com.github.christophpickl.snake4k.board.Matrix
import com.github.christophpickl.snake4k.model.Config
import com.github.christophpickl.snake4k.model.Fruit
import com.github.christophpickl.snake4k.model.Snake
import javafx.scene.canvas.Canvas
import javafx.scene.canvas.GraphicsContext
import javafx.scene.paint.Color
import java.awt.Dimension
import javax.inject.Inject

private val cellSize = 20
private val cellSizeAsDouble = cellSize.toDouble()
private val boardSize = Dimension(
    Config.countCols * cellSize + (Config.countCols - 1),
    Config.countRows * cellSize + (Config.countRows - 1)
)

class Board @Inject constructor(
    private val matrix: Matrix,
    private val snake: Snake,
    private val fruit: Fruit
) : Canvas(
    boardSize.width.toDouble(),
    boardSize.height.toDouble()
) {

    init {
        repaint()
    }

    fun repaint() {
        val g = graphicsContext2D
        g.stroke = Color.BLACK

        matrix.forEach { cell ->
            val rectX = cell.x * cellSizeAsDouble
            val rectY = cell.y * cellSizeAsDouble

            g.fill = cell.fillColor
            g.fillRect(rectX, rectY, cellSizeAsDouble, cellSizeAsDouble)

            if (cell.isOwnedBySnake()) {
                g.drawSnakeLines(cell, rectX, rectY)
            } else if (cell.isOwnedByFruit()) {
                g.cellStrokeLines(rectX, rectY, Position.all)
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

    fun nextFruitPosition(): Cell {
        var newPosition = matrix.randomCell()
        while (snake.contains(newPosition) || fruit.position == newPosition) {
            newPosition = matrix.randomCell()
        }
        return newPosition
    }

}

private fun GraphicsContext.cellStrokeLinesBy(cell: Cell, neighbour1: Cell?, neighbour2: Cell?, x: Double, y: Double) {
    val neighbourPositions = listOfNotNull(
        neighbour1?.let { cell.positionRelativeTo(it) },
        neighbour2?.let { cell.positionRelativeTo(it) }
    )
    cellStrokeLines(x, y, Position.all.minus(neighbourPositions))
}

private fun Cell.positionRelativeTo(that: Cell): Position =
    if (this.x - 1 == that.x && this.y == that.y) {
        Position.Left
    } else if (this.x + 1 == that.x && this.y == that.y) {
        Position.Right
    } else if (this.x == that.x && this.y - 1 == that.y) {
        Position.Top
    } else if (this.x == that.x && this.y + 1 == that.y) {
        Position.Bottom
    } else {
        throw IllegalArgumentException("Cell $this is not neighbour of $that")
    }

private fun GraphicsContext.cellStrokeLines(x: Double, y: Double, positions: List<Position>) {
    positions.forEach {
        cellStrokeLine(x, y, it)
    }
}

private fun GraphicsContext.cellStrokeLine(x: Double, y: Double, position: Position) {
    when (position) {
        Position.Top -> strokeLine(x, y, x + cellSize, y)
        Position.Bottom -> strokeLine(x, y + cellSize, x + cellSize, y + cellSize)
        Position.Left -> strokeLine(x, y, x, y + cellSize)
        Position.Right -> strokeLine(x + cellSize, y, x + cellSize, y + cellSize)
    }
}

private enum class Position {
    Top, Bottom, Left, Right;

    companion object {
        val all by lazy { values().toList() }
    }
}
