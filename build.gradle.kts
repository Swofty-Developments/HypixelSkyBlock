plugins {
    base
    id("io.freefair.lombok") version "8.6"
}

buildscript {
    repositories {
        mavenCentral()
        maven("https://plugins.gradle.org/m2/")
    }
}

allprojects {
    group = "net.swofty"
    version = "1.0"

    repositories {
        mavenLocal()
        mavenCentral()
    }
}

subprojects {
    apply(plugin = "io.freefair.lombok")
    apply(plugin = "java")
    apply(plugin = "java-library")
    apply(plugin = "application")

    plugins.withType<JavaPlugin> {
        repositories {
            mavenLocal()
            mavenCentral()
            gradlePluginPortal()

            maven("https://repo.viaversion.com")
        }

        dependencies {
            "implementation"("org.jetbrains.kotlin:kotlin-stdlib:1.7.10")
            "testImplementation"("org.junit.jupiter:junit-jupiter:5.8.2")

            "implementation"("org.reflections:reflections:0.10.2")
            "implementation"("com.fasterxml.jackson.core:jackson-databind:2.17.0")
            "implementation"("com.fasterxml.jackson.core:jackson-annotations:2.17.0")
            "implementation"("com.fasterxml.jackson.core:jackson-core:2.17.0")
            "implementation"("org.json:json:20240303")
            "implementation"("org.mongodb:mongo-java-driver:3.12.14")
            "implementation"("org.tinylog:tinylog-api:2.7.0")
            "implementation"("org.tinylog:tinylog-impl:2.7.0")
            "implementation"("org.tinylog:slf4j-tinylog:2.7.0")
            "compileOnly"("org.projectlombok:lombok:1.18.32")
        }

        tasks.withType<JavaCompile> {
            sourceCompatibility = JavaVersion.VERSION_21.toString()
            targetCompatibility = JavaVersion.VERSION_21.toString()
            options.encoding = "UTF-8"
        }

        tasks.withType<Test> {
            useJUnitPlatform()
        }
    }

    tasks.named<Zip>("distZip") {
        duplicatesStrategy = DuplicatesStrategy.INCLUDE
    }

    tasks.named<Tar>("distTar") {
        duplicatesStrategy = DuplicatesStrategy.INCLUDE
    }
}