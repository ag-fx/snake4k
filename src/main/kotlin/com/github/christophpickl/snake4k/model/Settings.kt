package com.github.christophpickl.snake4k.model

import javafx.beans.property.SimpleObjectProperty
import tornadofx.*

class Settings {
    val speedProperty = SimpleObjectProperty(this, "speed", Speed.Normal)
    var speed by speedProperty

    val bodyGrowProperty = SimpleObjectProperty(this, "bodyGrow", BodyGrow.Normal)
    var bodyGrow by bodyGrowProperty

}

class SettingsModel(settings: Settings) : ViewModel() {
    val speed = bind(propertyProducer = settings::speedProperty)
    val bodyGrow = bind(propertyProducer = settings::bodyGrowProperty)
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
    Little(1), Normal(2), Enormous(3);

    companion object {
        val all by lazy { values().toList() }
    }
}
