package com.github.christophpickl.snake4k.model

import javafx.beans.property.SimpleBooleanProperty
import javafx.beans.property.SimpleObjectProperty
import tornadofx.*
import java.awt.Dimension

class Settings {
    val speedProperty = SimpleObjectProperty(this, "speed", Speed.Normal)
    val speed: Speed by speedProperty

    val bodyGrowProperty = SimpleObjectProperty(this, "bodyGrow", BodyGrow.Normal)
    val bodyGrow: BodyGrow by bodyGrowProperty

    val mapSizeProperty = SimpleObjectProperty(this, "bodyGrow", MapSize.Normal)
    val mapSize: MapSize by mapSizeProperty

    val goThroughWallProperty = SimpleBooleanProperty(this, "goThroughWall", false)
    val goThroughWall: Boolean by goThroughWallProperty
}

class SettingsModel(settings: Settings) : ViewModel() {
    val speed = bind(propertyProducer = settings::speedProperty)
    val bodyGrow = bind(propertyProducer = settings::bodyGrowProperty)
    val mapSize = bind(propertyProducer = settings::mapSizeProperty)
    val goThroughWall = bind(propertyProducer = settings::goThroughWallProperty)
}

enum class Speed(
    val inMs: Long
) {
    Slow(200), Normal(100), Fast(70);

    companion object {
        val all by lazy { values().toList() }
    }
}

enum class BodyGrow(
    val factor: Int
) {
    Little(1), Normal(2), Enormous(4);

    companion object {
        val all by lazy { values().toList() }
    }
}

enum class MapSize(
    val cols: Int,
    val rows: Int
) {
    Tiny(15, 12), Normal(22, 20), Huge(33, 28);

    val boardSize = Dimension(cols * Config.cellSize, rows * Config.cellSize)

    companion object {
        val all by lazy { values().toList() }
    }
}
