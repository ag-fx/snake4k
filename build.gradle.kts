plugins {
    application
    kotlin("jvm") version "1.2.71"
}

dependencies {
    compile(kotlin("stdlib"))
    implementation("com.google.inject:guice:4.2.1")
}

repositories {
    jcenter()
}

application {
    mainClassName = "com.github.christophpickl.snake4k.Snake4k"
}

val fatJar = task("fatJar", type = Jar::class) {
    baseName = "${project.name}-fat"
    manifest {
        attributes["Main-Class"] = application.mainClassName
    }
    from(configurations.runtime.map { if (it.isDirectory) it else zipTree(it) })
    with(tasks["jar"] as CopySpec)
}

tasks {
    "build" {
        dependsOn(fatJar)
    }
}
