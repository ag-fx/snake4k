package com.github.christophpickl.snake4k

import com.github.christophpickl.snake4k.board.Board
import com.github.christophpickl.snake4k.board.Matrix
import com.github.christophpickl.snake4k.model.Fruit
import com.github.christophpickl.snake4k.model.Snake
import com.github.christophpickl.snake4k.view.KeyboardWatcher
import com.github.christophpickl.snake4k.view.Window
import java.awt.event.WindowAdapter
import java.awt.event.WindowEvent

object Snake4k {
    private val matrix = Matrix()
    private val keyboard = KeyboardWatcher()
    private val board: Board
    private val window: Window
    private val snake: Snake
    private val fruit: Fruit
    private val game: GameEngine

    init {
        val dummyCell = matrix.cellAt(0, 0)
        snake = Snake(dummyCell)
        fruit = Fruit(dummyCell)

        board = Board(matrix, snake, fruit)
        window = Window(board, ::restartGame, ::quit)
        window.addWindowListener(object : WindowAdapter() {
            override fun windowClosing(e: WindowEvent) {
                quit()
            }
        })
        window.addKeyListener(keyboard)
        game = GameEngine(matrix, board, window, snake, fruit, keyboard, ::quit)
    }

    @JvmStatic
    fun main(args: Array<String>) {
        window.isVisible = true
        restartGame()
    }

    private fun restartGame() {
        game.restart()
    }

    private fun quit() {
        Log.debug { "Quit application ..." }
        game.stop()
        window.isVisible = false
        window.dispose()
    }

}
