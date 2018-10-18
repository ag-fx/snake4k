import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    application
    kotlin("jvm") version "1.2.71"
}

dependencies {
    compile(kotlin("stdlib"))
    implementation("com.google.inject:guice:4.2.1")
    implementation("no.tornado:tornadofx:1.7.17")
}

repositories {
    jcenter()
}

application {
    mainClassName = "com.github.christophpickl.snake4k.Snake4k"
}

tasks.withType<KotlinCompile> {
    kotlinOptions.jvmTarget = "1.8"
}

// DISTRIBUTION
// =====================================================================================================================

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
