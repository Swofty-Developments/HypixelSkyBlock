plugins {
    base
    java
    id("io.freefair.lombok") version "9.1.0"
    id("io.sentry.jvm.gradle") version "5.12.2"
}

group = "net.swofty"
version = "1.0"

subprojects {
    apply(plugin = "java")
    apply(plugin = "java-library")
    apply(plugin = "io.freefair.lombok")

    repositories {
        mavenCentral()
        mavenLocal()
        maven("https://repo.viaversion.com")
        maven("https://jitpack.io")
        maven("https://repo.lucko.me/")
    }

    java {
        toolchain {
            languageVersion.set(JavaLanguageVersion.of(25))
        }
    }

    dependencies {
        testImplementation("org.junit.jupiter:junit-jupiter:6.0.2")

        implementation("org.reflections:reflections:0.10.2")
        implementation("org.json:json:20240303")
        implementation("io.sentry:sentry-async-profiler:8.30.0")

        compileOnly("org.projectlombok:lombok:1.18.42")

        implementation(platform("tools.jackson:jackson-bom:3.0.4"))
        implementation("tools.jackson.core:jackson-core")
        implementation("tools.jackson.core:jackson-databind")
        implementation("com.fasterxml.jackson.core:jackson-annotations:2.20")
    }

    tasks.test {
        useJUnitPlatform()
    }
}
