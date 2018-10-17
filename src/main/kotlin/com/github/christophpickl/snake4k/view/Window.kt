package com.github.christophpickl.snake4k.view

import com.github.christophpickl.snake4k.QuitEvent
import com.github.christophpickl.snake4k.RestartEvent
import com.github.christophpickl.snake4k.board.Board
import com.google.common.eventbus.EventBus
import java.awt.BorderLayout
import java.awt.Dimension
import java.awt.event.KeyEvent
import javax.inject.Inject
import javax.swing.BorderFactory
import javax.swing.JFrame
import javax.swing.JMenu
import javax.swing.JMenuBar
import javax.swing.JMenuItem
import javax.swing.JPanel
import javax.swing.KeyStroke
import javax.swing.WindowConstants

class Window @Inject constructor(
    board: Board,
    bus: EventBus
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
                        bus.post(RestartEvent)
                    }
                })
                add(JMenuItem("Quit").apply {
                    accelerator = KeyStroke.getKeyStroke(KeyEvent.VK_Q, KeyEvent.META_MASK, true)
                    addActionListener {
                        bus.post(QuitEvent)
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
