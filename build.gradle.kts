plugins {
    kotlin("jvm") version "1.9.0"
    application
    id("org.openjfx.javafxplugin") version "0.0.9"
}

group = "org.kudladev"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    testImplementation(kotlin("test"))
}

tasks.test {
    useJUnitPlatform()
}

kotlin {
    jvmToolchain(8)
}

javafx {
    modules = listOf("javafx.controls", "javafx.fxml", "javafx.web")
}

application {
    mainClass.set("MainKt")
}