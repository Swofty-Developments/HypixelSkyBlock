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
    maven("https://jitpack.io")
    maven("https://s01.oss.sonatype.org/content/repositories/snapshots/")
    maven("https://repo.viaversion.com")
}

dependencies {
    implementation(project(":commons"))
    implementation(project(":packer"))
    implementation(project(":proxy.api"))
    implementation(libs.mongodbBson)
    implementation(libs.mongodbDriverSync)
    // Must match AtlasRedisAPI's Jedis version to avoid conflicts
    implementation(libs.jedis)
    implementation(libs.tinylogApi)
    implementation(libs.tinylogImpl)
    implementation(libs.kotlinStdlib)
    compileOnly(libs.minestom) {
        exclude(group = "org.jboss.shrinkwrap.resolver", module = "shrinkwrap-resolver-depchain")
    }
    implementation(libs.adventureTextMinimessage)
    implementation(libs.caffeine)
    implementation(libs.polar)
    implementation(libs.snakeyaml)
    implementation(libs.fastutil)
}

tasks.test {
    useJUnitPlatform()
}