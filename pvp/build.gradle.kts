plugins {
    id("java")
}

group = "net.swofty"
version = "3.0"

repositories {
    mavenCentral()
}

dependencies {
    implementation("net.minestom:minestom:2025.12.20c-1.21.11") {
        exclude(group = "org.jboss.shrinkwrap.resolver", module = "shrinkwrap-resolver-depchain")
    }
    compileOnly("it.unimi.dsi:fastutil:8.5.18")
}

tasks.test {
    useJUnitPlatform()
}