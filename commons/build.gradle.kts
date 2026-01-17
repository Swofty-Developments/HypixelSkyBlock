plugins {
    java
    id("maven-publish")
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

dependencies {
    implementation("org.yaml:snakeyaml:2.5")
    implementation(project(":packer"))
    implementation("org.mongodb:bson:4.11.2")
    implementation("org.tinylog:tinylog-api:2.7.0")
    implementation("org.tinylog:tinylog-impl:2.7.0")

    compileOnly("net.minestom:minestom:2025.12.20c-1.21.11") {
        exclude(group = "org.jboss.shrinkwrap.resolver", module = "shrinkwrap-resolver-depchain")
    }

    implementation("org.spongepowered:configurate-yaml:4.2.0")
    implementation("com.squareup:javapoet:1.13.0")
}

sourceSets {
    val main by getting

    val codegen by creating {
        java.srcDir("src/codegen/java")

        compileClasspath += main.compileClasspath
        runtimeClasspath += main.compileClasspath
    }

    codegen // just here to avoid "unused" warning
}



val generateItemTypes by tasks.registering(JavaExec::class) {
    group = "codegen"
    description = "Generate ItemType enum from SkyBlock YAML"

    classpath = sourceSets["codegen"].runtimeClasspath
    mainClass.set("net.swofty.codegen.ItemTypeGenerator")

    args(
        rootProject.projectDir
            .resolve("configuration/skyblock/items")
            .absolutePath,
        layout.projectDirectory
            .dir("src/generated/java")
            .asFile
            .absolutePath
    )

    inputs.dir(rootProject.projectDir.resolve("configuration/skyblock/items"))
    outputs.dir(layout.projectDirectory.dir("src/generated/java"))
}

sourceSets["main"].java {
    srcDir(layout.projectDirectory.dir("src/generated/java"))
}

tasks.compileJava {
    dependsOn(generateItemTypes)
}
