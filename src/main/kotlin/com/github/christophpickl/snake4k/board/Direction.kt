package com.github.christophpickl.snake4k.board

import javafx.scene.input.KeyCode

enum class Direction(
    val keyCode: KeyCode,
    val coordinatesChange: Pair<Int, Int>
) {

    UP(KeyCode.UP, 0 to -1),
    DOWN(KeyCode.DOWN, 0 to 1),
    LEFT(KeyCode.LEFT, -1 to 0),
    RIGHT(KeyCode.RIGHT, 1 to 0);

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

        fun byKeyCode(keyCode: KeyCode) = directionsByKeyCode[keyCode]
    }
}
