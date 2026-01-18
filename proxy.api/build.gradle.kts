plugins {
    java
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

repositories {
    mavenCentral()
    maven("https://jitpack.io")
}

dependencies {
    implementation(project(":commons"))
    implementation("com.github.Swofty-Developments:AtlasRedisAPI:1.1.5")
    compileOnly("net.minestom:minestom:2025.12.20c-1.21.11") {
        exclude(group = "org.jboss.shrinkwrap.resolver", module = "shrinkwrap-resolver-depchain")
    }
    implementation("net.kyori:adventure-api:4.26.1")
    implementation("net.kyori:adventure-text-serializer-gson:4.26.1")
    implementation("org.tinylog:tinylog-api:2.7.0")
    implementation("org.tinylog:tinylog-impl:2.7.0")
}