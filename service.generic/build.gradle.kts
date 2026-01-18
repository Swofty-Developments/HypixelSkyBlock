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
    implementation("net.kyori:adventure-api:4.26.1")
    implementation("net.kyori:adventure-text-serializer-gson:4.26.1")
}