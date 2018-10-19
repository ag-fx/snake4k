package com.github.christophpickl.snake4k.model

import javafx.scene.paint.Color
import java.awt.Dimension

object Config {

    val bodyGrowFactorOnFruitEaten = 3

    val boardColor = Color.rgb(200, 220, 220)
    val snakeBodyColor = Color.rgb(50, 200, 50)
    val snakeHeadColor = Color.rgb(50, 150, 50)
    val fruitColor = Color.rgb(200, 80, 60)

    val countCols = 25
    val countRows = 20
    val cellSize = 20
    val cellSizeAsDouble = cellSize.toDouble()
    val boardSize = Dimension(
        countCols * cellSize,
        countRows * cellSize
    )
}
