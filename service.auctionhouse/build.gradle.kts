import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar

plugins {
    java
    application
    id("com.gradleup.shadow") version "9.3.1"
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

repositories {
    maven("https://jitpack.io")
    mavenCentral()
}

dependencies {
    implementation("com.github.ben-manes.caffeine:caffeine:3.2.3")
    implementation(project(":service.generic"))
    implementation(project(":type.skyblockgeneric"))
    implementation(project(":commons"))
    implementation("com.google.code.gson:gson:2.11.0")
    implementation("org.mongodb:bson:5.6.2")
    implementation("org.mongodb:mongodb-driver-sync:5.6.2")
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