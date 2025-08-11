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

repositories {
    maven("https://jitpack.io")
}

dependencies {
    implementation("com.github.ben-manes.caffeine:caffeine:3.1.8")
    implementation(project(":service.generic"))
    implementation(project(":type.skyblockgeneric"))
    implementation(project(":commons"))
    implementation("com.google.code.gson:gson:2.11.0")
}

application {
    mainClass.set("net.swofty.service.auction.AuctionService")
}

tasks {
    named<ShadowJar>("shadowJar") {
        archiveBaseName.set("ServiceAuctionHouse")
        archiveClassifier.set("")
        archiveVersion.set("")
    }
}