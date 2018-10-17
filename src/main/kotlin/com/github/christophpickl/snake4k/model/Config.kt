package com.github.christophpickl.snake4k.model

object Config {

    val tickTimeMs = 100L
    val bodyGrowFactorOnFruitEaten = 3

    val countCols = 25
    val countRows = 20

    val logEnabled = System.getProperty("snake4k.log") != null

}
