package com.github.christophpickl.snake4k.view

import com.github.christophpickl.snake4k.Log
import com.github.christophpickl.snake4k.board.Direction
import java.awt.event.KeyAdapter
import java.awt.event.KeyEvent
import java.util.LinkedList

class KeyboardWatcher : KeyAdapter() {

    val collectedDirections = LinkedList<Direction>()

    override fun keyPressed(e: KeyEvent) {
        Direction.byKeyCode(e.keyCode)?.let {
            Log.debug { "Pressed direction key: $it" }
            if (collectedDirections.isNotEmpty() && collectedDirections.last == it) {
                return
            }
            collectedDirections += it
        }
    }

}
