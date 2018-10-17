package com.github.christophpickl.snake4k.board

import java.awt.event.KeyEvent

enum class Direction(
    val keyCode: Int,
    val coordinatesChange: Pair<Int, Int>
) {

    UP(KeyEvent.VK_UP, 0 to -1),
    DOWN(KeyEvent.VK_DOWN, 0 to 1),
    LEFT(KeyEvent.VK_LEFT, -1 to 0),
    RIGHT(KeyEvent.VK_RIGHT, 1 to 0);

    private val neighbours by lazy {
        when (this) {
            Direction.UP -> setOf(LEFT, RIGHT)
            Direction.DOWN -> setOf(LEFT, RIGHT)
            Direction.LEFT -> setOf(UP, DOWN)
            Direction.RIGHT -> setOf(UP, DOWN)
        }
    }

    fun isNextTo(direction: Direction) = neighbours.contains(direction)

    companion object {
        private val directionsByKeyCode by lazy {
            values().associateBy { it.keyCode }
        }

        fun byKeyCode(keyCode: Int) = directionsByKeyCode[keyCode]
    }
}
