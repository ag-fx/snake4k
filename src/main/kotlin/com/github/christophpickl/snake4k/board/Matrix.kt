package com.github.christophpickl.snake4k.board

import com.github.christophpickl.snake4k.model.Settings
import java.util.Random
import javax.inject.Inject

class Matrix @Inject constructor(
    private val settings: Settings
) {

    private val rowsThanCols: ArrayList<ArrayList<Cell>> = ArrayList()
    private val random = Random()
    var rows: Int = -1
        private set
    var cols: Int = -1
        private set

    init {
        changeMatrix()
        settings.mapSizeProperty.addListener { _ ->
            changeMatrix()
        }
    }

    private fun changeMatrix() {
        rows = settings.mapSize.rows
        cols = settings.mapSize.cols

        rowsThanCols.clear()
        0.until(rows).forEach { y ->
            val row = ArrayList<Cell>()
            0.until(cols).forEach { x ->
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
        xy.first in 0..(cols - 1) && xy.second in 0..(rows - 1)

    fun randomCell() = cellAt(
        random.nextInt(cols),
        random.nextInt(rows)
    )
}
