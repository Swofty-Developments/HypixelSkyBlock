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
    implementation(libs.caffeine)
    implementation(project(":service.generic"))
    implementation(project(":type.generic"))
    implementation(project(":commons"))
    implementation(libs.gson)
    implementation(libs.sparkCore)
    implementation(libs.mongodbBson)
    implementation(libs.mongodbDriverSync)
    implementation(libs.tinylogApi)
    implementation(libs.tinylogImpl)
    compileOnly(libs.annotations)
}

application {
    mainClass.set("net.swofty.service.api.APIService")
}

tasks {
    named<ShadowJar>("shadowJar") {
        archiveBaseName.set("ServiceAPI")
        archiveClassifier.set("")
        archiveVersion.set("")
    }
}