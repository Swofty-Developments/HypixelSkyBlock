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
    implementation("com.github.Swofty-Developments:AtlasRedisAPI:1.1.2")
    implementation("net.minestom:minestom-snapshots:789befee31") {
        exclude(group = "org.jboss.shrinkwrap.resolver", module = "shrinkwrap-resolver-depchain")
    }
    implementation("net.kyori:adventure-api:4.17.0")
    implementation("net.kyori:adventure-text-serializer-gson:4.17.0")
}