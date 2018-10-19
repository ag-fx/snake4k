package com.github.christophpickl.snake4k.model

import javafx.beans.property.SimpleObjectProperty
import tornadofx.*

class Settings {
    val speedProperty = SimpleObjectProperty(this, "speed", Speed.Normal)
    var speed by speedProperty
}

class SettingsModel(settings: Settings) : ViewModel() {
    val speed = bind(propertyProducer = settings::speedProperty)
}

enum class Speed(
    val inMs: Long
) {
    Slow(500), Normal(200), Fast(100);

    companion object {
        val all by lazy { values().toList() }
    }
}
