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

dependencies {
    implementation(project(":type.skyblockgeneric"))
    implementation(project(":type.generic"))
    implementation(project(":commons"))
    implementation(project(":proxy.api"))
    compileOnly("net.minestom:minestom:2025.08.18-1.21.8") {
        exclude(group = "org.jboss.shrinkwrap.resolver", module = "shrinkwrap-resolver-depchain")
    }
    implementation("org.tinylog:tinylog-api:2.7.0")
    implementation("org.tinylog:tinylog-impl:2.7.0")
    implementation("org.joml:joml:1.10.8")
}