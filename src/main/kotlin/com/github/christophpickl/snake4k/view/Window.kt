package com.github.christophpickl.snake4k.view

import com.google.common.eventbus.EventBus
import java.awt.BorderLayout
import java.awt.Dimension
import javax.inject.Inject
import javax.swing.BorderFactory
import javax.swing.JFrame
import javax.swing.JPanel
import javax.swing.WindowConstants

class Windowx @Inject constructor(
    board: Board,
    bus: EventBus
) : JFrame() {
    private val padding = 10

    init {
        val mainPanel = JPanel(BorderLayout())
        mainPanel.border = BorderFactory.createEmptyBorder(padding, padding, padding, padding)
        mainPanel.add(board, BorderLayout.CENTER)
        rootPane.contentPane.add(mainPanel)

        defaultCloseOperation = WindowConstants.DO_NOTHING_ON_CLOSE
        size = Dimension(
            board.boardSize.width + padding + 14,
            board.boardSize.height + padding + 57
        )
        setLocationRelativeTo(null)
    }
}
