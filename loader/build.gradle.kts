import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar

plugins {
    java
    application
    id("io.github.goooler.shadow") version "8.1.7"
}

group = "net.swofty"
version = "3.0"

java {
    sourceCompatibility = JavaVersion.VERSION_17
    targetCompatibility = JavaVersion.VERSION_17
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(21))
    }
}

repositories {
    maven("https://jitpack.io")
}

dependencies {
    implementation(project(":type.island"))
    implementation(project(":type.hub"))
    implementation(project(":type.thefarmingislands"))
    implementation(project(":type.generic"))
    implementation(project(":service.protocol"))
    implementation(project(":commons"))
    implementation(project(":proxy.api"))
    implementation(project(":spark"))
    implementation("org.slf4j:slf4j-api:2.0.13")
    implementation("com.github.Minestom:Minestom:5c23713c03") {
        exclude(group = "org.jboss.shrinkwrap.resolver", module = "shrinkwrap-resolver-depchain")
    }
    implementation("dev.hollowcube:polar:1.7.2")
}

application {
    mainClass.set("net.swofty.loader.SkyBlock")
}

tasks {
    named<ShadowJar>("shadowJar") {
        archiveBaseName.set("SkyBlockCore")
        archiveClassifier.set("")
        archiveVersion.set("")
    }
}