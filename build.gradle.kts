import org.gradle.api.tasks.compile.JavaCompile
import org.gradle.api.tasks.testing.Test
import org.gradle.api.tasks.wrapper.Wrapper
import org.gradle.plugins.ide.idea.model.IdeaModel
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

apply {
    plugin("idea")
}

version = "5.2"

setBuildDir(file("build/gradle"))

var mediaDir: File by extra
mediaDir = file("media")

var externalLibsDir: File by extra
externalLibsDir = file("$buildDir/externalLibs")

val kotlinVersion: String by extra

buildscript {
    extra["kotlinVersion"] = "1.1.0"

    val mavenUrls = listOf("http://jcenter.bintray.com", "http://maven.ej-technologies.com/repository")
    extra["mavenUrls"] = mavenUrls

    repositories {
        flatDir {
            setDirs(listOf("lib-compile"))
        }
        maven {
            setUrl("http://dl.bintray.com/jetbrains/intellij-plugin-service")
        }
        for (mavenUrl in mavenUrls) {
            maven {
                setUrl(mavenUrl)
            }
        }
    }

    dependencies {
        classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:${extra["kotlinVersion"]}")
        classpath("org.jetbrains.dokka:dokka-gradle-plugin:0.9.13")

        // for local dokka in lib-compile
        //classpath ':dokka-fatjar'
        //classpath ':dokka-gradle-plugin'
        classpath("com.jfrog.bintray.gradle:gradle-bintray-plugin:1.7.3")
    }
}

val mavenUrls: List<String> by extra

subprojects {

    setBuildDir(File(rootProject.buildDir, path.substring(1).replace(':', '/')))

    group = "org.gjt.jclasslib"
    version = rootProject.version

    repositories {
        flatDir {
            setDirs(listOf(file("lib"), file("$rootDir/lib-compile")))
        }
        for (mavenUrl in mavenUrls) {
            maven {
                setUrl(mavenUrl)
            }
        }
    }

    plugins.withType<JavaPlugin> {
        dependencies {
            testCompile("org.testng:testng:6.8.8")
        }

        tasks.withType<JavaCompile>().forEach {compileJava ->
            compileJava.apply {
                sourceCompatibility = "1.8"
                targetCompatibility = "1.8"
            }
        }

        tasks.withType<Test>().forEach {test ->
            test.apply {
                useTestNG()
            }
        }

        tasks.withType<KotlinCompile> {
            kotlinOptions {
                languageVersion = "1.1"
            }
        }
    }
}

val clean by tasks.creating {
    doLast {
        delete(externalLibsDir)
    }
}

val dist by tasks.creating {}
val test by tasks.creating {}

tasks {
    "wrapper"(Wrapper::class) {
        gradleVersion = "3.4-rc-2"
        distributionType = Wrapper.DistributionType.ALL
    }
}

gradle.projectsEvaluated {
    getTasksByName("clean", true).forEach { task ->
        if (task != clean) {
            clean.dependsOn(task)
        }
    }
    getTasksByName("dist", true).forEach { task ->
        if (task != dist) {
            dist.dependsOn(task)
        }
    }
    getTasksByName("test", true).forEach { task ->
        if (task != test) {
            test.dependsOn(task)
        }
    }
}

val idea: IdeaModel by extensions
idea.apply {
    module {
        name = "root"
        excludeDirs = files("build", "dist", "media").files + excludeDirs
    }
}