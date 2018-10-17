package com.github.christophpickl.snake4k.model

import com.github.christophpickl.snake4k.board.Cell
import com.github.christophpickl.snake4k.board.Direction

class Snake(
    var head: Cell
) {
    val body = ArrayList<Cell>()
    var direction: Direction = Direction.RIGHT
    var growBody = 0

    fun removeLast() {
        if (body.isEmpty()) {
            return
        }
        body.removeAt(body.size - 1)
    }

    fun calculateNewHeadPosition() = Pair(
        head.x + direction.coordinatesChange.first,
        head.y + direction.coordinatesChange.second
    )

    fun contains(cell: Cell) = head == cell || body.contains(cell)

}
