import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar

plugins {
    java
    application
    id("com.gradleup.shadow") version "9.3.0"
}

group = "net.swofty"
version = "3.0"

java {
    sourceCompatibility = JavaVersion.VERSION_25
    targetCompatibility = JavaVersion.VERSION_25
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(25))
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