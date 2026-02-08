import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar

plugins {
    java
    application
    `maven-publish`
    id("com.gradleup.shadow") version "9.3.1"
    id("org.jetbrains.gradle.plugin.idea-ext") version "1.3"
}

java {
    sourceCompatibility = JavaVersion.VERSION_25
    targetCompatibility = JavaVersion.VERSION_25
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(25))
    }
}

repositories {
    maven("https://repo.papermc.io/repository/maven-public/")
    maven("https://jitpack.io")
    maven("https://s01.oss.sonatype.org/content/repositories/snapshots/")
    maven("https://repo.viaversion.com")
}

dependencies {
    compileOnly("com.velocitypowered:velocity-api:3.4.0-SNAPSHOT")
    annotationProcessor("com.velocitypowered:velocity-api:3.4.0-SNAPSHOT")
    compileOnly(files("velocity-proxy-3.4.0-SNAPSHOT.jar"))

    implementation("com.github.Swofty-Developments:AtlasRedisAPI:1.1.5")
    implementation(project(":commons"))
    implementation(project(":proxy.api"))
    implementation("org.mongodb:bson:5.6.2")
    implementation("org.mongodb:mongodb-driver-sync:5.6.2")

    implementation("com.viaversion:vialoader:4.0.6")
    implementation("com.viaversion:viabackwards-common:5.7.0")
    implementation("com.viaversion:viarewind-common:4.0.14")
    implementation("com.viaversion:viaversion:5.7.0")

    implementation(platform("io.netty:netty-bom:4.2.9.Final"))
    implementation("io.netty:netty-buffer")
    implementation("io.netty:netty-codec")
    implementation("io.netty:netty-codec-http")
    implementation("io.netty:netty-transport")
    implementation("io.netty:netty-handler")

    implementation("net.bytebuddy:byte-buddy:1.14.16")
    implementation("net.bytebuddy:byte-buddy-agent:1.14.16")
    implementation("org.tinylog:tinylog-api:2.7.0")
    implementation("org.tinylog:tinylog-impl:2.7.0")
}

evaluationDependsOn(":commons")

application {
    mainClass.set("net.swofty.velocity.SkyBlockVelocity")
}

tasks.withType<ShadowJar> {
    manifest {
        attributes["Main-Class"] = "net.swofty.velocity.SkyBlockVelocity"
    }
    archiveBaseName.set("SkyBlockProxy")
    archiveClassifier.set("")
    archiveVersion.set("")
}

publishing {
    publications {
        create<MavenPublication>("maven") {
            artifact(tasks["shadowJar"])
        }
    }
}