import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar

plugins {
    `java-library`
    application
    id("com.gradleup.shadow") version "9.3.2"
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

dependencies {
    api(libs.creative.api)
    api(libs.creative.serializer.minecraft)
    api(libs.creative.server)
}

application {
    mainClass.set("net.swofty.packer.HypixelPackServer")
}

tasks {
    named<ShadowJar>("shadowJar") {
        archiveBaseName.set("net.swofty.packer.HypixelPackServer")
        archiveClassifier.set("")
        archiveVersion.set("")
    }
}
