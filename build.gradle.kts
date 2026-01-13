plugins {
    base
    java
    id("io.freefair.lombok") version "9.1.0"
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

        compileOnly("org.projectlombok:lombok:1.18.42")

        implementation(platform("tools.jackson:jackson-bom:3.0.3"))
        implementation("tools.jackson.core:jackson-core")
        implementation("tools.jackson.core:jackson-databind")
        implementation("com.fasterxml.jackson.core:jackson-annotations:2.20")
    }

    tasks.test {
        useJUnitPlatform()
    }
}
