package com.github.christophpickl.snake4k.view

import com.github.christophpickl.snake4k.Log
import com.github.christophpickl.snake4k.board.Direction
import javafx.event.EventHandler
import java.util.LinkedList

class KeyboardWatcher : EventHandler<javafx.scene.input.KeyEvent> {

    val collectedDirections = LinkedList<Direction>()

    override fun handle(event: javafx.scene.input.KeyEvent) {
        Direction.byKeyCode(event.code)?.let {
            Log.debug { "Pressed direction key: $it" }
            if (collectedDirections.isNotEmpty() && collectedDirections.last == it) {
                return
            }
            collectedDirections += it
        }
    }

}
