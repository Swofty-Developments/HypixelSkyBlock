plugins {
    id("java")
}

group = "net.swofty"
version = "1.0"

java {
    sourceCompatibility = JavaVersion.VERSION_25
    targetCompatibility = JavaVersion.VERSION_25
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(25))
    }
}

repositories {
    maven("https://jitpack.io")
    maven("https://s01.oss.sonatype.org/content/repositories/snapshots/")
    maven("https://repo.viaversion.com")
}

dependencies {
    implementation(project(":commons"))
    implementation(project(":packer"))
    implementation(project(":proxy.api"))
    implementation("org.mongodb:bson:5.6.2")
    implementation("org.mongodb:mongodb-driver-sync:5.6.2")
    implementation("org.jline:jline-terminal:3.30.6")
    implementation("org.jline:jline-reader:3.30.6")
    // Must match AtlasRedisAPI's Jedis version to avoid conflicts
    implementation("redis.clients:jedis:4.2.3")
    implementation("org.tinylog:tinylog-api:2.7.0")
    implementation("org.tinylog:tinylog-impl:2.7.0")
    implementation("org.jetbrains.kotlin:kotlin-stdlib:2.3.0")
    compileOnly("net.minestom:minestom:2025.12.20c-1.21.11") {
        exclude(group = "org.jboss.shrinkwrap.resolver", module = "shrinkwrap-resolver-depchain")
    }
    implementation("dev.hollowcube:polar:1.15.0")
    implementation("org.yaml:snakeyaml:2.2")
    implementation("it.unimi.dsi:fastutil:8.5.18")
}

tasks.test {
    useJUnitPlatform()
}