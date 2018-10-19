package com.github.christophpickl.snake4k.view.components

import com.sun.javafx.tk.Toolkit
import javafx.scene.canvas.GraphicsContext
import javafx.scene.paint.Color
import javafx.scene.text.Font
import javafx.scene.text.FontWeight

class PauseOverlay(
    private val width: Double,
    private val height: Double
) {

    private val pauseFont = Font.font("arial", FontWeight.BOLD, 30.0)
    private val pauseText = "PAUSE"
    private val pauseWidth: Float
    private val pauseHeight: Float

    init {
        val metrics = Toolkit.getToolkit().fontLoader.getFontMetrics(pauseFont)
        pauseWidth = metrics.computeStringWidth(pauseText)
        pauseHeight = metrics.lineHeight
    }

    fun draw(g: GraphicsContext) {
        g.fill = Color.color(0.0, 0.0, 0.0, 0.3)
        g.fillRect(0.0, 0.0, width, height)

        g.fill = Color.WHITE
        g.font = pauseFont

        g.fillText(pauseText, (width - pauseWidth) / 2, (height - pauseHeight) / 2)
    }

}
