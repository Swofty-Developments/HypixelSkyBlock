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

    compileOnly(libs.minestom) {
        exclude(group = "org.jboss.shrinkwrap.resolver", module = "shrinkwrap-resolver-depchain")
    }

    implementation(libs.tinylog.api)
    implementation(libs.tinylog.impl)

    testImplementation(platform("org.junit:junit-bom:5.10.0"))
    testImplementation("org.junit.jupiter:junit-jupiter")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
}

tasks.test {
    useJUnitPlatform()
}