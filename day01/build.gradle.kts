plugins {
    kotlin("jvm")
    application
}

repositories {
    mavenCentral()
}

// Configure the application plugin
application {
    mainClass.set("Day01Kt")
}

// Recommended: create a typed run task
tasks.named<JavaExec>("run") {
    group = "application"
    description = "Run Day01 main class"
    classpath = sourceSets["main"].runtimeClasspath
}
