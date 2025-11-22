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
    mavenCentral()
    maven("https://jitpack.io")
}

dependencies {
    implementation(project(":commons"))
    implementation("com.github.Swofty-Developments:AtlasRedisAPI:1.1.3")
    // implementation("net.swofty:AtlasRedisAPI:1.1.4")
    compileOnly("net.minestom:minestom:2025.08.18-1.21.8") {
        exclude(group = "org.jboss.shrinkwrap.resolver", module = "shrinkwrap-resolver-depchain")
    }
    implementation("net.kyori:adventure-api:4.24.0")
    implementation("net.kyori:adventure-text-serializer-gson:4.24.0")
    implementation("org.tinylog:tinylog-api:2.7.0")
    implementation("org.tinylog:tinylog-impl:2.7.0")
}