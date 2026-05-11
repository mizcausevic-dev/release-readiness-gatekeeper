package com.mizcausevic.releasereadiness

import io.javalin.util.JavalinBindException
import kotlin.system.exitProcess

fun main() {
    val port = System.getenv("PORT")?.toIntOrNull() ?: 4428
    val app = ReleaseReadinessGatekeeperApp.create()
    try {
        app.start("127.0.0.1", port)
    } catch (_: JavalinBindException) {
        println("Release Readiness Gatekeeper could not start because port $port is already in use.")
        println("Set a different port before running again, for example:")
        println("${'$'}env:PORT = \"4432\"")
        println(".\\gradlew.bat run")
        app.stop()
        exitProcess(1)
    }
}
