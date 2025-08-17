plugins {
    id("java")
}

group = "net.swofty"
version = "1.0"

repositories {
    maven("https://jitpack.io")
    maven("https://s01.oss.sonatype.org/content/repositories/snapshots/")
    maven("https://repo.viaversion.com")
}

dependencies {
    implementation(project(":commons"))
    implementation(project(":packer"))
    implementation(project(":proxy.api"))
    implementation("org.mongodb:bson:4.11.2")
    implementation("org.mongodb:mongodb-driver-sync:4.11.2")
    implementation("org.tinylog:tinylog-api:2.7.0")
    implementation("org.tinylog:tinylog-impl:2.7.0")
    implementation("org.jetbrains.kotlin:kotlin-stdlib:1.9.22")
    compileOnly("net.minestom:minestom-snapshots:1_21_4-7599413490") {
        exclude(group = "org.jboss.shrinkwrap.resolver", module = "shrinkwrap-resolver-depchain")
    }
    implementation("dev.hollowcube:polar:1.14.0")
    implementation("org.yaml:snakeyaml:2.2")
}

tasks.test {
    useJUnitPlatform()
}