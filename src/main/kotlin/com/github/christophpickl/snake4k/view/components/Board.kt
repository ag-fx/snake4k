package com.github.christophpickl.snake4k.view.components

import com.github.christophpickl.snake4k.model.GameState
import com.github.christophpickl.snake4k.model.Settings
import com.github.christophpickl.snake4k.model.State
import javafx.scene.canvas.Canvas
import javafx.scene.paint.Color
import mu.KotlinLogging
import javax.inject.Inject

class Board @Inject constructor(
    private val state: State,
    private val matrixDrawer: MatrixDrawer,
    private val pauseOverlay: PauseOverlay,
    private val settings: Settings
) : Canvas(settings.mapSize.boardSize.getWidth(), settings.mapSize.boardSize.getHeight()) {

    private val log = KotlinLogging.logger {}

    init {
        repaint()
        settings.mapSizeProperty.addListener { _ ->
            changeSize()
        }
    }

    fun repaint() {
        val g = graphicsContext2D
        matrixDrawer.draw(g)
        if (!settings.goThroughWall) {
            g.stroke = Color.GRAY
            g.strokeRect(0.0, 0.0, width, height)
        }
        if (state.gameState == GameState.Paused) {
            pauseOverlay.draw(g)
        }
    }

    private fun changeSize() {
        log.trace { "changeSize => ${settings.mapSize}" }
        width = settings.mapSize.boardSize.getWidth()
        height = settings.mapSize.boardSize.getHeight()
        repaint()
    }
}
