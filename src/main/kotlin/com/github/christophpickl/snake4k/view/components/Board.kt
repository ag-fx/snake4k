package com.github.christophpickl.snake4k.view.components

import com.github.christophpickl.snake4k.model.GameState
import com.github.christophpickl.snake4k.model.State
import com.google.inject.BindingAnnotation
import javafx.scene.canvas.Canvas
import java.awt.Dimension
import javax.inject.Inject

class Board @Inject constructor(
    private val state: State,
    private val matrixDrawer: MatrixDrawer,
    private val pauseOverlay: PauseOverlay,
    @BoardSize size: Dimension
) : Canvas(
    size.width.toDouble(),
    size.height.toDouble()
) {

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

@BindingAnnotation
@Target(AnnotationTarget.FIELD, AnnotationTarget.CONSTRUCTOR, AnnotationTarget.VALUE_PARAMETER)
@Retention(AnnotationRetention.RUNTIME)
annotation class BoardSize
