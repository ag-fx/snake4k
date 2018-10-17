package com.github.christophpickl.snake4k.board

import com.github.christophpickl.snake4k.Config
import com.github.christophpickl.snake4k.Config.countCols
import com.github.christophpickl.snake4k.Config.countRows
import java.util.Random

class Matrix {

    private val data: ArrayList<ArrayList<Cell>> = ArrayList() // rows than cols
    private val random = Random()

    init {
        0.until(Config.countRows).forEach { y ->
            val row = ArrayList<Cell>()
            0.until(Config.countCols).forEach { x ->
                row += Cell(x, y)
            }
            data += row
        }
    }

    fun cellAt(xy: Pair<Int, Int>) = cellAt(xy.first, xy.second)

    fun cellAt(x: Int, y: Int) = data[y][x]

    fun forEach(action: (Cell) -> Unit) {
        data.forEach { row ->
            row.forEach {
                action(it)
            }
        }
    }

    fun cellExists(xy: Pair<Int, Int>) =
        xy.first in 0..(countCols-1) && xy.second in 0..(countRows-1)

    fun randomCell() = cellAt(
        random.nextInt(Config.countCols),
        random.nextInt(Config.countRows)
    )
}
