package com.github.christophpickl.snake4k.model

import com.github.christophpickl.snake4k.board.Cell
import com.github.christophpickl.snake4k.board.Direction
import javax.inject.Inject

class Snake @Inject constructor(
    private val settings: Settings
) {

    var head: Cell = Cell.none
    val body = ArrayList<Cell>()
    var direction: Direction = Direction.Right
    var growBody = 0



    fun contains(cell: Cell) = head == cell || body.contains(cell)

    fun move(newHeadProvider: () -> Cell) {
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

        head = newHeadProvider()
    }

    private fun ArrayList<*>.removeLast() {
        removeAt(size - 1)
    }
}
