import org.gradle.jvm.tasks.Jar

plugins {
    alias(libs.plugins.fabric.loom)
    `maven-publish`
}

version = "0.1.0"
group = "gg.itzkatze.hypixelrecreationmod"

base {
    archivesName.set("hypixelrecreationmod")
}

loom {
    splitEnvironmentSourceSets()

    mods {
        create("thehypixelrecreationmod") {
            sourceSet(sourceSets["client"])
        }
    }
}

sourceSets {
    named("main") {
        java.setSrcDirs(emptyList<String>())
        resources.setSrcDirs(emptyList<String>())
    }

    named("client") {
        java.setSrcDirs(listOf("src/main/java"))
        resources.setSrcDirs(listOf("src/main/resources"))
    }
}

repositories {
    exclusiveContent {
        forRepository {
            maven("https://api.modrinth.com/maven")
        }

        filter {
            includeGroup("maven.modrinth")
        }
    }

    maven("https://repo.papermc.io/repository/maven-public/")
    maven("https://repo.hypixel.net/repository/Hypixel/")

    maven {
        name = "sonatype-oss-snapshots1"
        url = uri(
            "https://s01.oss.sonatype.org/content/repositories/snapshots/"
        )

        mavenContent {
            snapshotsOnly()
        }
    }

    mavenLocal()
}

val shaded = configurations.create("shaded") {
    isCanBeResolved = true
    isCanBeConsumed = false
}

configurations.named("implementation") {
    extendsFrom(shaded)
}

dependencies {
    minecraft(libs.minecraft)

    implementation(libs.fabric.loader)
    implementation(libs.joml)
    include(libs.joml)

    shaded(libs.adventure.text.serializer.legacy)
    shaded(libs.adventure.api)
    shaded(libs.adventure.text.logger.slf4j)
    shaded(libs.adventure.nbt)

    shaded(libs.minestom) {
        exclude(group = "net.kyori")
    }

    shaded(libs.polar) {
        exclude(group = "net.kyori")
    }

    implementation(libs.hypixel.mod.api)
    implementation(libs.hypixel.mod.api.fabric)

    implementation(libs.fabric.api)
    implementation(libs.adventure.platform.fabric)
    include(libs.adventure.platform.fabric)
}

val copyShadedDependencies =
    tasks.register<Copy>("copyShadedDependencies") {
        description = "copies shaded dependencies"
        from(shaded)
        into(layout.buildDirectory.dir("libs/shaded"))
    }

tasks.named("build") {
    dependsOn(copyShadedDependencies)
}

tasks.named<Jar>("jar") {
    from({
        shaded.map { dependency ->
            if (dependency.isDirectory) {
                dependency
            } else {
                zipTree(dependency)
            }
        }
    }) {
        exclude("META-INF/**")
        exclude("net/minestom/server/adventure/provider/**")
    }

    duplicatesStrategy = DuplicatesStrategy.EXCLUDE
}

tasks.withType<JavaCompile>().configureEach {
    options.release.set(25)
}

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(25))
    }

    withSourcesJar()
}
