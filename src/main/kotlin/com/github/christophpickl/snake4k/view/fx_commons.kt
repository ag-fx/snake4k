package com.github.christophpickl.snake4k.view

import javafx.scene.control.Alert
import javafx.scene.control.ButtonType
import javafx.stage.Window

fun buildAlert(
    type: Alert.AlertType,
    header: String,
    content: String? = null,
    vararg buttons: ButtonType,
    owner: Window? = null,
    title: String? = null
): Alert {
    val alert = Alert(type, content ?: "", *buttons)
    title?.let { alert.title = it }
    alert.headerText = header
    owner?.also { alert.initOwner(it) }
    return alert
}
