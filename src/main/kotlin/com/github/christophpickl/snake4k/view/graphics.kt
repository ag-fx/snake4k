package com.github.christophpickl.snake4k.view

import com.github.christophpickl.snake4k.board.Cell
import com.github.christophpickl.snake4k.board.Direction
import com.github.christophpickl.snake4k.model.Config
import javafx.scene.canvas.GraphicsContext

fun GraphicsContext.cellStrokeLinesBy(cell: Cell, neighbour1: Cell?, neighbour2: Cell?, x: Double, y: Double) {
    val neighbourPositions = listOfNotNull(
        neighbour1?.let { cell.positionRelativeTo(it) },
        neighbour2?.let { cell.positionRelativeTo(it) }
    )
    cellStrokeLines(x, y, Direction.all.minus(neighbourPositions))
}

fun Cell.positionRelativeTo(that: Cell): Direction =
    if (this.x - 1 == that.x && this.y == that.y) {
        Direction.Left
    } else if (this.x + 1 == that.x && this.y == that.y) {
        Direction.Right
    } else if (this.x == that.x && this.y - 1 == that.y) {
        Direction.Up
    } else if (this.x == that.x && this.y + 1 == that.y) {
        Direction.Down
    } else {
        throw IllegalArgumentException("Cell $this is not neighbour of $that")
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
