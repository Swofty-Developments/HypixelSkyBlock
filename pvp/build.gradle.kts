plugins {
    id("java")
}

group = "net.swofty"
version = "3.0"

repositories {
    mavenCentral()
}

dependencies {
    implementation("net.minestom:minestom-snapshots:42e0d21266") {
        exclude(group = "org.jboss.shrinkwrap.resolver", module = "shrinkwrap-resolver-depchain")
    }
}

tasks.test {
    useJUnitPlatform()
}