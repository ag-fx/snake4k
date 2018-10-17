package com.github.christophpickl.snake4k.view

import com.github.christophpickl.snake4k.board.Cell
import com.github.christophpickl.snake4k.board.Matrix
import com.github.christophpickl.snake4k.model.Config
import com.github.christophpickl.snake4k.model.Fruit
import com.github.christophpickl.snake4k.model.Snake
import java.awt.Color
import java.awt.Dimension
import java.awt.Graphics
import javax.inject.Inject
import javax.swing.JPanel

class Board @Inject constructor(
    private val matrix: Matrix,
    private val snake: Snake,
    private val fruit: Fruit
) : JPanel() {

    init {
        background = Color.WHITE
    }

    private val sizeEachCell = 20
    private val gapCells = 2

    val boardSize = Dimension(
        Config.countCols * sizeEachCell + ((Config.countCols - 1) * gapCells),
        Config.countRows * sizeEachCell + ((Config.countRows - 1) * gapCells)
    )

    private val defaultColor = Color(205, 220, 220)
    private val snakeBodyColor = Color(225, 225, 60)
    private val snakeHeadColor = Color(205, 205, 60)
    private val fruitColor = Color(205, 85, 65)

    override fun paint(g: Graphics) {
        matrix.forEach { cell ->
            g.color = when {
                snake.body.contains(cell) -> snakeBodyColor
                snake.head == cell -> snakeHeadColor
                fruit.position == cell -> fruitColor
                else -> defaultColor
            }
            g.fillRect(
                sizeEachCell * cell.x + gapCells * cell.x,
                sizeEachCell * cell.y + gapCells * cell.y,
                sizeEachCell, sizeEachCell
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
