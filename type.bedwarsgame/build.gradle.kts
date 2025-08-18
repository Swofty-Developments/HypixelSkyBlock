plugins {
    java
}

group = "net.swofty"
version = "3.0"

repositories {
    maven("https://jitpack.io")
    maven("https://s01.oss.sonatype.org/content/repositories/snapshots/")
    maven("https://repo.viaversion.com")
}

dependencies {
    implementation(project(":type.bedwarsgeneric"))
    implementation(project(":type.generic"))
    implementation(project(":commons"))
    implementation(project(":proxy.api"))
    implementation("org.mongodb:bson:4.11.2")
    implementation("net.kyori:adventure-text-minimessage:4.21.0")
    implementation("dev.hollowcube:polar:1.14.0")
    compileOnly("net.minestom:minestom-snapshots:1_21_4-7599413490") {
        exclude(group = "org.jboss.shrinkwrap.resolver", module = "shrinkwrap-resolver-depchain")
    }
    implementation("org.tinylog:tinylog-api:2.7.0")
    implementation("org.tinylog:tinylog-impl:2.7.0")
}

tasks.withType<JavaCompile> {
    options.compilerArgs.add("--enable-preview")
}