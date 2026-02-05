plugins {
    id("java")
}

group = "net.swofty"
version = "1.0"

repositories {
    mavenCentral()
}

dependencies {
    implementation(project(":commons"))

    compileOnly("net.minestom:minestom:2025.12.20c-1.21.11") {
        exclude(group = "org.jboss.shrinkwrap.resolver", module = "shrinkwrap-resolver-depchain")
    }

    implementation("org.tinylog:tinylog-api:2.7.0")
    implementation("org.tinylog:tinylog-impl:2.7.0")

    testImplementation(platform("org.junit:junit-bom:5.10.0"))
    testImplementation("org.junit.jupiter:junit-jupiter")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
}

tasks.test {
    useJUnitPlatform()
}