plugins {
    java
    id("maven-publish")
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

dependencies {
    implementation("org.yaml:snakeyaml:2.2")
    implementation(project(":packer"))
    implementation("org.mongodb:bson:4.11.2")
    implementation("org.tinylog:tinylog-api:2.7.0")
    implementation("org.tinylog:tinylog-impl:2.7.0")

    compileOnly("net.minestom:minestom:2025.12.20c-1.21.11") {
        exclude(group = "org.jboss.shrinkwrap.resolver", module = "shrinkwrap-resolver-depchain")
    }

    // Must match AtlasRedisAPI's Jedis version to avoid conflicts
    implementation("redis.clients:jedis:4.2.3")

    implementation("org.spongepowered:configurate-yaml:4.2.0")
}