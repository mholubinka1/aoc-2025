plugins {
    kotlin("jvm")
    application
}

repositories {
    mavenCentral()
}

// Configure the application plugin
application {
    mainClass.set("Day06Kt")
}

// Recommended: create a typed run task
tasks.named<JavaExec>("run") {
    group = "application"
    description = "Run (day06) main class"
    classpath = sourceSets["main"].runtimeClasspath
}

tasks.withType<JavaExec> {
    standardInput = System.`in`
}
