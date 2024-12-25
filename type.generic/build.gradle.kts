plugins {
    java
}

group = "net.swofty"
version = "3.0"

java {
    sourceCompatibility = JavaVersion.VERSION_21
    targetCompatibility = JavaVersion.VERSION_21
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(21))
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
    implementation("net.minestom:minestom-snapshots:f71ab6d851") {
        exclude(group = "org.jboss.shrinkwrap.resolver", module = "shrinkwrap-resolver-depchain")
    }
    implementation("dev.hollowcube:polar:1.10.0")
    implementation("org.yaml:snakeyaml:2.2")
}

tasks.withType<JavaCompile> {
    options.compilerArgs.add("--enable-preview")
}