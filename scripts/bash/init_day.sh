#!/usr/bin/env bash

# Requires jq
# Defaults to current day if no argument provided
DAY="${1:-$(date +%d)}"
DAY=$(printf "%02d" "$DAY")  # Pad to 2 digits

# Paths
REPO_ROOT="$(cd "$(dirname "$0")/../.." && pwd)"
DAY_ID="day$DAY"
DAY_PATH="$REPO_ROOT/$DAY_ID"

if [ -d "$DAY_PATH" ]; then
    echo -e "\033[36mDirectory for $DAY_ID already exists. Exiting.\033[0m"
    exit 1
fi

echo -e "\033[32mCreating directory for $DAY_ID...\033[0m"
mkdir -p "$DAY_PATH/src/main/kotlin"

# --- build.gradle.kts ---
cat > "$DAY_PATH/build.gradle.kts" <<EOF
plugins {
    kotlin("jvm")
    application
}

repositories {
    mavenCentral()
}

// Configure the application plugin
application {
    mainClass.set("Day${DAY}Kt")
}

// Optional: typed run task
tasks.named<JavaExec>("run") {
    group = "application"
    description = "Run Day${DAY} main class"
    classpath = sourceSets["main"].runtimeClasspath
}
EOF

# --- Kotlin template ---
cat > "$DAY_PATH/src/main/kotlin/Day${DAY}.kt" <<EOF
fun main() {
    println("Hello World! From Day $DAY!")
}
EOF

# --- settings.gradle.kts ---
SETTINGS_FILE="$REPO_ROOT/settings.gradle.kts"
echo "include(\":$DAY_ID\")" >> "$SETTINGS_FILE"
echo -e "\033[32mAdded $DAY_ID to settings.gradle.kts.\033[0m"

# --- VS Code launch.json ---
VSCODE_DIR="$REPO_ROOT/.vscode"
mkdir -p "$VSCODE_DIR"
LAUNCH_JSON="$VSCODE_DIR/launch.json"

if [ ! -f "$LAUNCH_JSON" ]; then
    echo '{ "version": "0.2.0", "configurations": [] }' > "$LAUNCH_JSON"
fi

# Add new configuration
jq --arg day "$DAY" --arg dayId "$DAY_ID" '
    .configurations += [
        {
            "type": "java",
            "name": "Debug AoC \($dayId)",
            "request": "launch",
            "mainClass": "Day\($day)Kt",
            "preLaunchTask": "Gradle: Build \($dayId)",
            "console": "integratedTerminal",
            "classPaths": ["${workspaceFolder}/\($dayId)/build/classes/kotlin/main"]
        }
    ]
' "$LAUNCH_JSON" > "$LAUNCH_JSON.tmp" && mv "$LAUNCH_JSON.tmp" "$LAUNCH_JSON"

echo -e "\033[32mAdded VS Code launch configuration for $DAY_ID\033[0m"

# --- VS Code tasks.json ---
TASKS_JSON="$VSCODE_DIR/tasks.json"
if [ ! -f "$TASKS_JSON" ]; then
    echo '{ "version": "2.0.0", "tasks": [] }' > "$TASKS_JSON"
fi

jq --arg dayId "$DAY_ID" \
   --arg cmd ".\/gradlew :$DAY_ID:build" \
   --arg winCmd ".\\gradlew.bat :$DAY_Id:build" '
    .tasks += [
        {
            "label": "Gradle: Build \($dayId)",
            "type": "shell",
            "command": $cmd,
            "windows": {
                "command": $winCmd
            },
            "group": "build",
            "problemMatcher": ["$gradle"]
        }
    ]
' "$TASKS_JSON" > "$TASKS_JSON.tmp" && mv "$TASKS_JSON.tmp" "$TASKS_JSON"

echo -e "\033[32mAdded VS Code tasks configuration for $DAY_ID\033[0m"
echo -e "\033[32mDirectory for $DAY_ID created successfully.\033[0m"
