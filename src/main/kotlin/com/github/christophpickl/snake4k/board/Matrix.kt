package com.github.christophpickl.snake4k.board

import com.github.christophpickl.snake4k.model.Config
import com.github.christophpickl.snake4k.model.Config.countCols
import com.github.christophpickl.snake4k.model.Config.countRows
import java.util.Random

class Matrix {

    private val rowsThanCols: ArrayList<ArrayList<Cell>> = ArrayList()
    private val random = Random()

    init {
        0.until(Config.countRows).forEach { y ->
            val row = ArrayList<Cell>()
            0.until(Config.countCols).forEach { x ->
                row += Cell(x, y)
            }
            rowsThanCols += row
        }
    }

    fun cellAt(xy: Pair<Int, Int>) = cellAt(xy.first, xy.second)
    fun cellAt(x: Int, y: Int) = rowsThanCols[y][x]

    fun forEach(action: (Cell) -> Unit) {
        rowsThanCols.forEach { row ->
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
