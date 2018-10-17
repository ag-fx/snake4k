package com.github.christophpickl.snake4k.view

import com.github.christophpickl.snake4k.board.Board
import java.awt.BorderLayout
import java.awt.Dimension
import java.awt.event.KeyEvent
import javax.swing.BorderFactory
import javax.swing.JFrame
import javax.swing.JMenu
import javax.swing.JMenuBar
import javax.swing.JMenuItem
import javax.swing.JPanel
import javax.swing.KeyStroke
import javax.swing.WindowConstants

class Window(
    board: Board,
    onRestartGame: () -> Unit,
    onQuit: () -> Unit
) : JFrame() {
    private val padding = 10

    init {
        val mainPanel = JPanel(BorderLayout())
        mainPanel.border = BorderFactory.createEmptyBorder(padding, padding, padding, padding)
        mainPanel.add(board, BorderLayout.CENTER)
        rootPane.contentPane.add(mainPanel)

        jMenuBar = JMenuBar().apply {
            add(JMenu("Snake4k").apply {
                add(JMenuItem("Restart Game").apply {
                    accelerator = KeyStroke.getKeyStroke(KeyEvent.VK_R, KeyEvent.META_MASK, true)
                    addActionListener {
                        onRestartGame()
                    }
                })
                add(JMenuItem("Quit").apply {
                    accelerator = KeyStroke.getKeyStroke(KeyEvent.VK_Q, KeyEvent.META_MASK, true)
                    addActionListener {
                        onQuit()
                    }
                })
            })
        }

        defaultCloseOperation = WindowConstants.DO_NOTHING_ON_CLOSE
        size = Dimension(
            board.boardSize.width + padding + 14,
            board.boardSize.height + padding + 57
        )
        setLocationRelativeTo(null)
    }
}
