plugins {
    kotlin("jvm")
    application
}

repositories {
    mavenCentral()
}

// Configure the application plugin
application {
    mainClass.set("Day07Kt")
}

// Recommended: create a typed run task
tasks.named<JavaExec>("run") {
    group = "application"
    description = "Run (day07) main class"
    classpath = sourceSets["main"].runtimeClasspath
}
