import org.jetbrains.intellij.IntelliJPluginExtension
import org.jetbrains.intellij.tasks.PublishTask

plugins {
    id("org.jetbrains.intellij") version "0.2.1"
}

buildscript {
    dependencies {
        repositories {
            maven {
                setUrl("http://dl.bintray.com/jetbrains/intellij-plugin-service")
            }
        }
    }
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

configure<IntelliJPluginExtension> {
    version = "IC-2016.2.5"
    pluginName = "jclasslib"
    setPlugins("ByteCodeViewer", "org.jetbrains.kotlin:$kotlinVersion-IJ2016.2-1@EAP-1.1")
    sandboxDirectory = "${rootProject.buildDir}/../idea_sandbox"
    updateSinceUntilBuild = false
}

tasks {
    "publishPlugin"(PublishTask::class) {
        setUsername(project.findProperty("intellij.publish.username") ?: "")
        setPassword(project.findProperty("intellij.publish.password") ?: "")
    }
}