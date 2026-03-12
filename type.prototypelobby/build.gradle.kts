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

    implementation(libs.mongodbBson)
    implementation(libs.mongodbDriverSync)
    implementation(libs.tinylogApi)
    implementation(libs.tinylogImpl)
    implementation(libs.minestom) {
        exclude(group = "org.jboss.shrinkwrap.resolver", module = "shrinkwrap-resolver-depchain")
    }
}
