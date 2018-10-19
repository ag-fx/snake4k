package com.github.christophpickl.snake4k.view

import com.github.christophpickl.snake4k.board.Direction
import javafx.event.EventHandler
import javafx.scene.input.KeyEvent
import mu.KotlinLogging
import java.util.LinkedList

class KeyboardWatcher : EventHandler<KeyEvent> {

    private val log = KotlinLogging.logger {}

    val collectedDirections = LinkedList<Direction>()

    override fun handle(event: KeyEvent) {
        Direction.byKeyCode(event.code)?.let {
            log.debug { "Pressed direction key: $it" }
            if (collectedDirections.isEmpty() || collectedDirections.last != it) {
                collectedDirections += it
            }
        }
    }

}
