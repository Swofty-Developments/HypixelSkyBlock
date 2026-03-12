plugins {
    java
}

group = "net.swofty"
version = "3.0"

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
    implementation(project(":type.generic"))
    implementation(libs.mongodbBson)
    implementation(libs.mongodbDriverSync)
    implementation(libs.tinylogApi)
    implementation(libs.tinylogImpl)
    implementation(libs.minestom) {
        exclude(group = "org.jboss.shrinkwrap.resolver", module = "shrinkwrap-resolver-depchain")
    }
    implementation(libs.polar)
    implementation(libs.fastutil)
    implementation(libs.snakeyaml)
    implementation(libs.kotlinStdlib)
}
