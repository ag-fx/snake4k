package com.github.christophpickl.snake4k.board

data class Cell(
    val x: Int,
    val y: Int
) {
    val xy = x to y

    companion object {
        val none = Cell(-1, -1)
    }
}
