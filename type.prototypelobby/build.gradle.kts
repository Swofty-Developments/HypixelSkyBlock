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
    mavenCentral()
}

dependencies {
    implementation(project(":type.lobby"))
    implementation(project(":commons"))
    implementation(project(":proxy.api"))
    implementation(project(":type.generic"))

    implementation(libs.mongodb.bson)
    implementation(libs.mongodb.driver.sync)
    implementation(libs.tinylog.api)
    implementation(libs.tinylog.impl)
    implementation(libs.minestom) {
        exclude(group = "org.jboss.shrinkwrap.resolver", module = "shrinkwrap-resolver-depchain")
    }
}
