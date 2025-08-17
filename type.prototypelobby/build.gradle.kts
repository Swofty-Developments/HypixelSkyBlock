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
    implementation(project(":packer"))
    implementation(project(":proxy.api"))
    implementation(project(":type.generic"))
    implementation("net.minestom:minestom-snapshots:1_21_4-7599413490") {
        exclude(group = "org.jboss.shrinkwrap.resolver", module = "shrinkwrap-resolver-depchain")
    }
}

tasks.withType<JavaCompile> {
    options.compilerArgs.add("--enable-preview")
}