package com.github.christophpickl.snake4k.view.components

import com.github.christophpickl.snake4k.board.Matrix
import com.github.christophpickl.snake4k.model.Config
import com.github.christophpickl.snake4k.model.Fruit
import com.github.christophpickl.snake4k.model.GameState
import com.github.christophpickl.snake4k.model.Snake
import com.github.christophpickl.snake4k.model.State
import javafx.scene.canvas.Canvas
import javax.inject.Inject

// TODO inject MatrixDrawer and PauseOverlay directly (make boardSize an injectable bean, with annotation)
class Board @Inject constructor(
    private val state: State,
    matrix: Matrix,
    snake: Snake,
    fruit: Fruit
) : Canvas(
    Config.boardSize.width.toDouble(),
    Config.boardSize.height.toDouble()
) {

    private val matrixDrawer = MatrixDrawer(matrix, snake, fruit)
    private val pauseOverlay = PauseOverlay(width, height)

    init {
        repaint()
    }

    fun repaint() {
        val g = graphicsContext2D
        matrixDrawer.draw(g)

        if (state.gameState == GameState.Paused) {
            pauseOverlay.draw(g)
        }
    }

}
