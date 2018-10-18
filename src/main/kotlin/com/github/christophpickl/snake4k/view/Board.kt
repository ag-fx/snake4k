package com.github.christophpickl.snake4k.view

import com.github.christophpickl.snake4k.board.Cell
import com.github.christophpickl.snake4k.board.Matrix
import com.github.christophpickl.snake4k.model.Config
import com.github.christophpickl.snake4k.model.Fruit
import com.github.christophpickl.snake4k.model.Snake
import java.awt.Dimension
import javax.inject.Inject

private val sizeEachCell = 20
private val sizeEachCellToDouble = sizeEachCell.toDouble()
private val gapCells = 2
private val boardSize = Dimension(
    Config.countCols * sizeEachCell + ((Config.countCols - 1) * gapCells),
    Config.countRows * sizeEachCell + ((Config.countRows - 1) * gapCells)
)

class Board @Inject constructor(
    private val matrix: Matrix,
    private val snake: Snake,
    private val fruit: Fruit
) : javafx.scene.canvas.Canvas(
    boardSize.width + 14.0,
    boardSize.height + 57.0
) {

    private val defaultColor = javafx.scene.paint.Color.rgb(205, 220, 220)
    private val snakeBodyColor = javafx.scene.paint.Color.rgb(225, 225, 60)
    private val snakeHeadColor = javafx.scene.paint.Color.rgb(205, 205, 60)
    private val fruitColor = javafx.scene.paint.Color.rgb(205, 85, 65)

    init {
        repaint()
    }

    fun repaint() {
        val g = graphicsContext2D
        matrix.forEach { cell ->
            g.fill = when {
                snake.body.contains(cell) -> snakeBodyColor
                snake.head == cell -> snakeHeadColor
                fruit.position == cell -> fruitColor
                else -> defaultColor
            }
            g.fillRect(
                (sizeEachCell * cell.x + gapCells * cell.x).toDouble(),
                (sizeEachCell * cell.y + gapCells * cell.y).toDouble(),
                sizeEachCellToDouble, sizeEachCellToDouble
            )
        }
    }

    fun nextFruitPosition(): Cell {
        var newPosition = matrix.randomCell()
        while (snake.contains(newPosition) || fruit.position == newPosition) {
            newPosition = matrix.randomCell()
        }
        return newPosition
    }

}
