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
    implementation(project(":service.generic"))
    implementation(project(":commons"))
    implementation(libs.caffeine)
    implementation(libs.tinylogApi)
    implementation(libs.tinylogImpl)
    implementation(libs.gson)
    implementation(libs.mongodbBson)
    implementation(libs.mongodbDriverSync)
}

application {
    mainClass.set("net.swofty.service.friend.FriendService")
}

tasks {
    named<ShadowJar>("shadowJar") {
        archiveBaseName.set("ServiceFriend")
        archiveClassifier.set("")
        archiveVersion.set("")
    }
}
