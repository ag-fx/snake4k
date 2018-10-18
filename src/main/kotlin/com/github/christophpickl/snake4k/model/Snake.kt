package com.github.christophpickl.snake4k.model

import com.github.christophpickl.snake4k.board.Cell
import com.github.christophpickl.snake4k.board.Direction

class Snake {

    var head: Cell = Cell.empty
    val body = ArrayList<Cell>()
    var direction: Direction = Direction.RIGHT
    var growBody = 0

    fun calculateNewHeadPosition() = Pair(
        head.x + direction.coordinatesChange.first,
        head.y + direction.coordinatesChange.second
    )

    fun contains(cell: Cell) = head == cell || body.contains(cell)

    fun move(cellProvider: (Pair<Int, Int>) -> Cell) {
        var willAddBody = false
        if (growBody == 0) {
            if (body.isNotEmpty()) {
                willAddBody = true
                body.removeLast()
            }
        } else {
            willAddBody = true
            growBody--
        }
        if (willAddBody) {
            body.add(0, head)
        }

        val newPos = calculateNewHeadPosition()
        val newHead = cellProvider(newPos)
        head = newHead
    }

    private fun ArrayList<*>.removeLast() {
        removeAt(size - 1)
    }
}
