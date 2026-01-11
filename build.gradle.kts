plugins {
    base
    id("io.freefair.lombok") version "9.1.0"
}

buildscript {
    repositories {
        mavenCentral()
        maven("https://plugins.gradle.org/m2/")
    }
}

allprojects {
    group = "net.swofty"
    version = "1.0"

    repositories {
        mavenLocal()
        mavenCentral()
    }
}

subprojects {
    apply(plugin = "io.freefair.lombok")
    apply(plugin = "java")
    apply(plugin = "java-library")
    apply(plugin = "application")

    plugins.withType<JavaPlugin> {
        repositories {
            mavenLocal()
            mavenCentral()
            gradlePluginPortal()

            maven("https://repo.viaversion.com")
            maven("https://jitpack.io")
        }

        dependencies {
            "testImplementation"("org.junit.jupiter:junit-jupiter:6.0.2")

            "implementation"("org.reflections:reflections:0.10.2")
            "implementation"("com.fasterxml.jackson.core:jackson-databind:2.20.1")
            "implementation"("com.fasterxml.jackson.core:jackson-annotations:2.20.1")
            "implementation"("com.fasterxml.jackson.core:jackson-core:2.20.1")
            "implementation"("org.json:json:20240303")
            "compileOnly"("org.projectlombok:lombok:1.18.42")
        }

        tasks.withType<JavaCompile> {
            sourceCompatibility = JavaVersion.VERSION_25.toString()
            targetCompatibility = JavaVersion.VERSION_25.toString()
            options.encoding = "UTF-8"
        }

        tasks.withType<Test> {
            useJUnitPlatform()
        }
    }

    tasks.named<Zip>("distZip") {
        duplicatesStrategy = DuplicatesStrategy.INCLUDE
    }

    tasks.named<Tar>("distTar") {
        duplicatesStrategy = DuplicatesStrategy.INCLUDE
    }
}