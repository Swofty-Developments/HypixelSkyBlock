import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar

plugins {
    java
    application
    id("io.github.goooler.shadow") version "8.1.7"
}

group = "net.swofty"
version = "3.0"

java {
    sourceCompatibility = JavaVersion.VERSION_21
    targetCompatibility = JavaVersion.VERSION_21
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(21))
    }
}

application {
    mainClass.set("net.swofty.packer.SkyBlockPacker")
}

tasks {
    named<ShadowJar>("shadowJar") {
        archiveBaseName.set("net.swofty.packer.SkyBlockPacker")
        archiveClassifier.set("")
        archiveVersion.set("")
    }
}