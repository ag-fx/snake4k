package com.github.christophpickl.snake4k.logic

import com.github.christophpickl.snake4k.board.Matrix
import com.github.christophpickl.snake4k.model.Config
import com.github.christophpickl.snake4k.model.Fruit
import com.github.christophpickl.snake4k.model.Settings
import com.github.christophpickl.snake4k.model.Snake
import com.github.christophpickl.snake4k.model.State
import com.github.christophpickl.snake4k.view.KeyboardWatcher
import com.github.christophpickl.snake4k.view.components.Board
import com.github.christophpickl.snake4k.view.components.BoardSize
import com.github.christophpickl.snake4k.view.components.MatrixDrawer
import com.github.christophpickl.snake4k.view.components.PauseOverlay
import com.google.common.eventbus.EventBus
import com.google.inject.AbstractModule
import java.awt.Dimension

class GuiceModule : AbstractModule() {
    override fun configure() {
        bind(EventBus::class.java).toInstance(EventBus())
        bind(ApplicationManager::class.java).asEagerSingleton()

        bind(State::class.java).asEagerSingleton()
        bind(Settings::class.java).asEagerSingleton()
        bind(Snake::class.java).asEagerSingleton()
        bind(Fruit::class.java).asEagerSingleton()

        bind(Matrix::class.java).asEagerSingleton()
        bind(KeyboardWatcher::class.java).asEagerSingleton()
        bind(Board::class.java).asEagerSingleton()
        bind(MatrixDrawer::class.java).asEagerSingleton()
        bind(Dimension::class.java)
            .annotatedWith(BoardSize::class.java)
            .toInstance(Dimension(Config.boardSize.width, Config.boardSize.height))
        bind(PauseOverlay::class.java).asEagerSingleton()

        bind(GameEngine::class.java).asEagerSingleton()
        bind(GameLogic::class.java).asEagerSingleton()

    }
}
