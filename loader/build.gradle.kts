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
    implementation(project(":commons"))
    implementation(project(":proxy.api"))
    implementation(project(":spark"))
    implementation(project(":anticheat"))
    implementation("org.slf4j:slf4j-api:2.0.13")
    implementation("net.minestom:minestom-snapshots:1_21_4-7599413490") {
        exclude(group = "org.jboss.shrinkwrap.resolver", module = "shrinkwrap-resolver-depchain")
    }
    implementation("dev.hollowcube:polar:1.12.1")
}

application {
    mainClass.set("net.swofty.loader.SkyBlock")
    applicationDefaultJvmArgs = listOf("--enable-preview", "-Duser.dir=${rootProject.projectDir}")
}

tasks {
    named<ShadowJar>("shadowJar") {
        archiveBaseName.set("SkyBlockCore")
        archiveClassifier.set("")
        archiveVersion.set("")
    }
}

val serverType: String by project

tasks.register<JavaExec>("runServer") {
    group = "application"
    description = "Runs the application with the specified server type"

    dependsOn("build")

    javaLauncher.set(javaToolchains.launcherFor {
        languageVersion.set(JavaLanguageVersion.of(21))
    })

    doFirst {
        if (!project.hasProperty("serverType")) {
            throw GradleException("Please provide a server type using -PserverType=<type>")
        }
    }
    workingDir = rootProject.projectDir

    mainClass.set("net.swofty.loader.SkyBlock")
    classpath = sourceSets["main"].runtimeClasspath
    jvmArgs("--enable-preview")
    args(serverType)

    doLast {
        println("Application started with server type: $serverType")
    }
}

tasks.named("runServer").configure {
    mustRunAfter("build")
    onlyIf {
        project.gradle.startParameter.taskNames.contains("runServer")
    }
}