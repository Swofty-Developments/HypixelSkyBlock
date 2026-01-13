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

dependencies {
    implementation("net.minestom:minestom:2025.12.20c-1.21.11") {
        exclude(group = "org.jboss.shrinkwrap.resolver", module = "shrinkwrap-resolver-depchain")
    }
    implementation(project(":type.generic"))
    implementation("com.google.guava:guava:33.5.0-jre")
    api("me.lucko:spark-common:1.10.158-20260110.094844-1")
}

application {
    mainClass.set("net.swofty.spark.Spark")
}

tasks.withType<ShadowJar> {
    archiveBaseName.set("SkyBlockSpark")
    archiveClassifier.set("")
    archiveVersion.set("")
}