package com.github.christophpickl.snake4k.board

import javafx.scene.input.KeyCode

enum class Direction(
    val keyCode: KeyCode,
    val coordinatesChange: Pair<Int, Int>
) {

    Up(KeyCode.UP, 0 to -1),
    Down(KeyCode.DOWN, 0 to 1),
    Left(KeyCode.LEFT, -1 to 0),
    Right(KeyCode.RIGHT, 1 to 0);

    companion object {
        val all by lazy { values().toList() }

        private val directionsByKeyCode by lazy {
            values().associateBy { it.keyCode }
        }

        fun byKeyCode(keyCode: KeyCode) = directionsByKeyCode[keyCode]
    }

    fun isNextTo(direction: Direction) = neighbours.contains(direction)

    private val neighbours by lazy {
        when (this) {
            Direction.Up -> setOf(Left, Right)
            Direction.Down -> setOf(Left, Right)
            Direction.Left -> setOf(Up, Down)
            Direction.Right -> setOf(Up, Down)
        }
    }
}
