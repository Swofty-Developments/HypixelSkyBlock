plugins {
    java
    id("maven-publish")
}

group = "net.swofty"
version = "3.0"

java {
    sourceCompatibility = JavaVersion.VERSION_17
    targetCompatibility = JavaVersion.VERSION_17
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(21))
    }
}

dependencies {
    implementation("org.yaml:snakeyaml:2.2")
    implementation(project(":packer"))
    implementation("net.minestom:minestom-snapshots:1_21_4-7599413490") {
        exclude(group = "org.jboss.shrinkwrap.resolver", module = "shrinkwrap-resolver-depchain")
    }
}