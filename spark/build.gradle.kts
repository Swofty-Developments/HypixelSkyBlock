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
    implementation("net.minestom:minestom-snapshots:4553d3c574") {
        exclude(group = "org.jboss.shrinkwrap.resolver", module = "shrinkwrap-resolver-depchain")
    }
    implementation(files("dependencies/spark-1.10.1.10-minestom.jar"))
    implementation(project(":type.generic"))
}

application {
    mainClass.set("net.swofty.spark.Spark")
}

tasks.withType<ShadowJar> {
    archiveBaseName.set("SkyBlockSpark")
    archiveClassifier.set("")
    archiveVersion.set("")
}