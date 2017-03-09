import org.jetbrains.intellij.tasks.PublishTask

plugins {
    id("org.jetbrains.intellij") version "0.2.1"
}

apply {
    plugin("kotlin")
}

val kotlinVersion = rootProject.extra["kotlinVersion"]
dependencies {
    compile(project(":browser"))
    // explicit Kotlin dependency to prevent the intellij plugin from adding the Kotlin libraries in the lib directory
    compile("org.jetbrains.kotlin:kotlin-stdlib:$kotlinVersion")
}

intellij {
    version = "IC-2016.2.5"
    pluginName = "jclasslib"
    // See https://plugins.jetbrains.com/plugin/6954-kotlin for kotlin plugin versions
    // Add @EAP-1.1 to the version string for selecting a channel
    setPlugins("ByteCodeViewer", "org.jetbrains.kotlin:$kotlinVersion-release-IJ2016.2-1")
    sandboxDirectory = "${rootProject.buildDir}/../idea_sandbox"
    updateSinceUntilBuild = false
}

tasks {
    "publishPlugin"(PublishTask::class) {
        setUsername(project.findProperty("intellij.publish.username") ?: "")
        setPassword(project.findProperty("intellij.publish.password") ?: "")
    }
}