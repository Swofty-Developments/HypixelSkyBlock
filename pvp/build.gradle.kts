plugins {
    id("java")
}

group = "net.swofty"
version = "3.0"

repositories {
    mavenCentral()
}

dependencies {
    implementation("net.minestom:minestom:2025.08.18-1.21.8") {
        exclude(group = "org.jboss.shrinkwrap.resolver", module = "shrinkwrap-resolver-depchain")
    }
}

tasks.test {
    useJUnitPlatform()
}