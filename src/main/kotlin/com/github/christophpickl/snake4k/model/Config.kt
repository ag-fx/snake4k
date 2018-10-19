package com.github.christophpickl.snake4k.model

import javafx.scene.paint.Color
import java.awt.Dimension

object Config {

    val tickTimeMs = 100L
    val bodyGrowFactorOnFruitEaten = 3

    val countCols = 25
    val countRows = 20

    val boardColor = Color.rgb(200, 220, 220)
    val snakeBodyColor = Color.rgb(50, 200, 50)
    val snakeHeadColor = Color.rgb(50, 150, 50)
    val fruitColor = Color.rgb(200, 80, 60)

    val cellSize = 20
    val cellSizeAsDouble = cellSize.toDouble()
    val boardSize = Dimension(
        countCols * cellSize + (countCols - 1),
        countRows * cellSize + (countRows - 1)
    )
}
