package com.github.christophpickl.snake4k.view

import com.github.christophpickl.snake4k.board.Direction
import com.github.christophpickl.snake4k.model.GameState
import com.github.christophpickl.snake4k.model.State
import javafx.event.EventHandler
import javafx.scene.input.KeyEvent
import mu.KotlinLogging
import java.util.LinkedList
import javax.inject.Inject

class KeyboardWatcher @Inject constructor(
    private val state: State
) : EventHandler<KeyEvent> {

    private val log = KotlinLogging.logger {}

    val collectedDirections = LinkedList<Direction>()

    override fun handle(event: KeyEvent) {
        if (state.gameState != GameState.Running) {
            return
        }
        Direction.byKeyCode(event.code)?.let {
            log.debug { "Pressed direction key: $it" }
            if (collectedDirections.isEmpty() || collectedDirections.last != it) {
                collectedDirections += it
            }
        }
    }

}
