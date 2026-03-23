import org.gradle.api.artifacts.VersionCatalog
import org.gradle.api.artifacts.VersionCatalogsExtension
import org.gradle.kotlin.dsl.getByType

plugins {
    base
    java
    id("io.freefair.lombok") version "9.1.0"
    id("io.sentry.jvm.gradle") version "5.12.2"
}

group = "net.swofty"
version = "1.0"

val libsCatalog: VersionCatalog = extensions.getByType<VersionCatalogsExtension>().named("libs")

subprojects {
    apply(plugin = "java")
    apply(plugin = "java-library")
    apply(plugin = "io.freefair.lombok")

    repositories {
        mavenCentral()
        mavenLocal()
        maven("https://repo.viaversion.com")
        maven("https://jitpack.io")
        maven("https://repo.lucko.me/")
    }

    java {
        toolchain {
            languageVersion.set(JavaLanguageVersion.of(25))
        }
    }

    dependencies {
        testImplementation(libsCatalog.findLibrary("junit-jupiter").get())

        implementation(libsCatalog.findLibrary("reflections").get())
        implementation(libsCatalog.findLibrary("json").get())
        implementation(libsCatalog.findLibrary("sentry-async-profiler").get())

        compileOnly(libsCatalog.findLibrary("lombok").get())

        implementation(platform(libsCatalog.findLibrary("jackson-bom").get()))
        implementation(libsCatalog.findLibrary("jackson-core").get())
        implementation(libsCatalog.findLibrary("jackson-databind").get())
        implementation(libsCatalog.findLibrary("jackson-annotations").get())
    }

    tasks.test {
        useJUnitPlatform()
    }
}
