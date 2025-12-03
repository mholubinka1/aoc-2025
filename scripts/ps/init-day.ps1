param(
    [int]$Day = (Get-Date).Day
)

$RepoRoot = "$PSScriptRoot\..\.."
Set-Location $RepoRoot

$dayId = "day" + $Day.ToString().PadLeft(2, '0')
$dayPath = Join-Path $RepoRoot $dayId

if (Test-Path $dayPath) {
    Write-Host "Directoy for $dayId already exists. Exiting." -ForegroundColor Cyan
    exit 1
}

Write-Host "Creating directory for $dayId..." -ForegroundColor Green
New-Item -ItemType Directory -Path $dayPath | Out-Null
New-Item -ItemType Directory -Path "$dayPath/src/main/kotlin" -Force | Out-Null

$buildGradle = @"
plugins {
    kotlin("jvm")
    application
}

repositories {
    mavenCentral()
}

// Configure the application plugin
application {
    mainClass.set("Day$($Day.ToString("00"))Kt")
}

// Recommended: create a typed run task
tasks.named<JavaExec>("run") {
    group = "application"
    description = "Run ($dayId) main class"
    classpath = sourceSets["main"].runtimeClasspath
}
"@
Set-Content -Path "$dayPath/build.gradle.kts" -Value $buildGradle

$kotlinFile = @"
fun main() {
    println("Hello World! From Day $($Day.ToString("00"))!")
}
"@
Set-Content -Path "$dayPath/src/main/kotlin/Day$($Day.ToString("00")).kt" -Value $kotlinFile

$settingsFile = Join-Path $RepoRoot "settings.gradle.kts"
$includeLine = "include("":$dayId"")"
Add-Content -Path $settingsFile -Value $includeLine

Write-Host "Added $dayId to settings.gradle.kts." -ForegroundColor Green

$launchJson = Join-Path $RepoRoot ".vscode\launch.json"
if (-Not (Test-Path $launchJson)) {
    $initialJson = @{
        version = "0.2.0"
        configurations = @()
    } | ConvertTo-Json -Depth 10

    Set-Content -Path $launchJson -Value $initialJson
}

$json = Get-Content -Path $launchJson -Raw | ConvertFrom-Json

$newConfig = @{
    type = "java"
    name = "Debug AoC $dayId"
    request = "launch"
    mainClass = "Day$($Day.ToString("00"))Kt"
    preLaunchTask = "Gradle: Build $dayId"
    console = "integratedTerminal"
    classPaths = @("`${workspaceFolder}/$($dayId)/build/classes/kotlin/main")
}

$json.configurations += $newConfig

$json | ConvertTo-Json -Depth 10 | Set-Content -Path $launchJson

Write-Host "Added VS Code launch configuration for $dayId" -ForegroundColor Green

$tasksJson = Join-Path $RepoRoot ".vscode\tasks.json"
if (-Not (Test-Path $tasksJson)) {
    $initialTasks = @{
        version = "2.0.0"
        tasks = @()
    } | ConvertTo-Json -Depth 10

    Set-Content -Path $tasksJson -Value $initialTasks
}

$tasks = Get-Content -Path $tasksJson -Raw | ConvertFrom-Json

$newTask = @{
    label = "Gradle: Build $dayId"
    type = "shell"
    group = "build"
    problemMatcher = @('$gradle')
    command = "./gradlew.bat :$($dayId):build"
    windows = @{
        command = ".\gradlew.bat :$($dayId):build"
    }
}

$tasks.tasks += $newTask

$tasks | ConvertTo-Json -Depth 10 | Set-Content -Path $tasksJson

Write-Host "Added VS Code tasks configuration for $dayId" -ForegroundColor Green

Write-Host "Directory for $dayId created successfully." -ForegroundColor Green