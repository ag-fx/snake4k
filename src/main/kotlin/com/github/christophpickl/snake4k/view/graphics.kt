package com.github.christophpickl.snake4k.view

import com.github.christophpickl.snake4k.board.Cell
import com.github.christophpickl.snake4k.board.Direction
import com.github.christophpickl.snake4k.model.Config
import javafx.scene.canvas.GraphicsContext

fun GraphicsContext.cellStrokeLinesBy(cell: Cell, neighbour1: Cell?, neighbour2: Cell?, x: Double, y: Double, rows: Int, cols: Int) {
    val neighbourPositions = listOfNotNull(
        neighbour1?.let { cell.positionRelativeTo(it, rows, cols) },
        neighbour2?.let { cell.positionRelativeTo(it, rows, cols) }
    )
    cellStrokeLines(x, y, Direction.all.minus(neighbourPositions))
}

fun Cell.positionRelativeTo(that: Cell, rows: Int, cols: Int): Direction =
    if (this.y == that.y && (this.x - 1 == that.x || this.x == (cols - 1) && that.x == 0)) {
        Direction.Left
    } else if (this.y == that.y && (this.x + 1 == that.x || this.x == 0 && that.x == (cols - 1))) {
        Direction.Right
    } else if (this.x == that.x && (this.y - 1 == that.y || this.y == 0 && that.y == (rows - 1))) {
        Direction.Up
    } else if (this.x == that.x && (this.y + 1 == that.y || this.y == (rows - 1) && that.y == 0)) {
        Direction.Down
    } else {
        throw IllegalArgumentException("$this is not neighbour of $that")
    }

fun GraphicsContext.cellStrokeLines(x: Double, y: Double, positions: List<Direction>) {
    positions.forEach {
        cellStrokeLine(x, y, it)
    }
}

fun GraphicsContext.cellStrokeLine(x: Double, y: Double, position: Direction) {
    when (position) {
        Direction.Up -> strokeLine(x, y, x + Config.cellSize, y)
        Direction.Down -> strokeLine(x, y + Config.cellSize, x + Config.cellSize, y + Config.cellSize)
        Direction.Left -> strokeLine(x, y, x, y + Config.cellSize)
        Direction.Right -> strokeLine(x + Config.cellSize, y, x + Config.cellSize, y + Config.cellSize)
    }
}
