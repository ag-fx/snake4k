package com.github.christophpickl.snake4k.logic

import ch.qos.logback.classic.Level
import ch.qos.logback.classic.LoggerContext
import ch.qos.logback.classic.encoder.PatternLayoutEncoder
import ch.qos.logback.classic.filter.ThresholdFilter
import ch.qos.logback.classic.spi.ILoggingEvent
import ch.qos.logback.core.ConsoleAppender
import org.slf4j.Logger
import org.slf4j.LoggerFactory

private val context = LoggerFactory.getILoggerFactory() as LoggerContext
private val defaultPattern = "%-43(%d{yyyy-MM-dd HH:mm:ss.SSS} [%thread]) [%-5level] %logger{42} - %msg%n"

fun configureLogging() {
    if (System.getProperty("snake4k.log") == null) {
        return
    }
    val rootLogger = context.getLogger(Logger.ROOT_LOGGER_NAME)
    rootLogger.detachAndStopAllAppenders()
    rootLogger.level = Level.ALL

    configurePackageLevels()
    rootLogger.addAppender(buildConsoleAppender())
}

private fun buildConsoleAppender() = ConsoleAppender<ILoggingEvent>().also {
    it.context = context
    it.name = "ConsoleLog"
    it.encoder = patternLayout(defaultPattern)
    it.start()
    it.addFilter(ThresholdFilter().apply {
        setLevel(Level.ALL.levelStr)
        start()
    })
}

private fun configurePackageLevels() {
//    mapOf(
//        "foo.bar" to Level.WARN
//    ).forEach { packageName, level ->
//        context.getLogger(packageName).level = level
//    }
}

private fun patternLayout(pattern: String): PatternLayoutEncoder {
    val layout = PatternLayoutEncoder()
    layout.context = context
    layout.pattern = pattern
    layout.start()
    return layout
}
