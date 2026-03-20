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
    implementation(libs.minestom) {
        exclude(group = "org.jboss.shrinkwrap.resolver", module = "shrinkwrap-resolver-depchain")
    }
    implementation(project(":type.generic"))
    implementation(libs.guava)
    api(libs.spark.common)
}

application {
    mainClass.set("net.swofty.spark.Spark")
}

tasks.withType<ShadowJar> {
    archiveBaseName.set("SkyBlockSpark")
    archiveClassifier.set("")
    archiveVersion.set("")
}