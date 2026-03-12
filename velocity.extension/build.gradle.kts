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
    compileOnly(libs.velocityApi)
    annotationProcessor(libs.velocityApi)
    compileOnly(files("velocity-3.5.0-SNAPSHOT-576.jar"))

    implementation(libs.atlasRedis)
    implementation(project(":commons"))
    implementation(project(":proxy.api"))
    implementation(libs.mongodbBson)
    implementation(libs.mongodbDriverSync)

    implementation(libs.vialoader)
    implementation(libs.viabackwardsCommon)
    implementation(libs.viarewindCommon)
    implementation(libs.viaversion)

    implementation(platform(libs.nettyBom))
    implementation(libs.nettyBuffer)
    implementation(libs.nettyCodec)
    implementation(libs.nettyCodecHttp)
    implementation(libs.nettyTransport)
    implementation(libs.nettyHandler)

    implementation(libs.byteBuddy)
    implementation(libs.byteBuddyAgent)
    implementation(libs.tinylogApi)
    implementation(libs.tinylogImpl)
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