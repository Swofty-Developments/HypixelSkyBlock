plugins {
    java
}

group = "net.swofty"
version = "3.0"

java {
    sourceCompatibility = JavaVersion.VERSION_21
    targetCompatibility = JavaVersion.VERSION_21
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(21))
    }
}

repositories {
    maven("https://jitpack.io")
}

dependencies {
    implementation(project(":commons"))
    implementation(project(":type.generic"))
    implementation(project(":service.protocol"))
    implementation("com.github.Swofty-Developments:AtlasRedisAPI:1.1.2")
    implementation("net.kyori:adventure-api:4.17.0")
    implementation("net.kyori:adventure-text-serializer-gson:4.17.0")
    implementation("net.kyori:adventure-text-serializer-json:4.17.0")
}