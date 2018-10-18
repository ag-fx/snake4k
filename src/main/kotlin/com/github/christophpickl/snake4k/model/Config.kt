package com.github.christophpickl.snake4k.model

import javafx.scene.paint.Color

object Config {

    val tickTimeMs = 100L
    val bodyGrowFactorOnFruitEaten = 3

    val countCols = 25
    val countRows = 20

    val logEnabled = System.getProperty("snake4k.log") != null


    val boardColor = Color.rgb(205, 220, 220)
    val snakeBodyColor = Color.rgb(225, 225, 60)
    val snakeHeadColor = Color.rgb(205, 205, 60)
    val fruitColor = Color.rgb(205, 85, 65)

}
