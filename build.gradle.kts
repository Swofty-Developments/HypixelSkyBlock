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

val deployableProjects = linkedMapOf(
    ":loader" to "HypixelCore.jar",
    ":velocity.extension" to "SkyBlockProxy.jar",
    ":service.api" to "ServiceAPI.jar",
    ":service.auctionhouse" to "ServiceAuctionHouse.jar",
    ":service.bazaar" to "ServiceBazaar.jar",
    ":service.darkauction" to "ServiceDarkAuction.jar",
    ":service.datamutex" to "ServiceDataMutex.jar",
    ":service.friend" to "ServiceFriend.jar",
    ":service.itemtracker" to "ServiceItemTracker.jar",
    ":service.orchestrator" to "ServiceOrchestrator.jar",
    ":service.party" to "ServiceParty.jar",
    ":service.punishment" to "ServicePunishment.jar"
)

val availableDeployableProjects = deployableProjects.filterKeys { path ->
    findProject(path) != null
}

tasks.register<Sync>("assembleDeploymentArtifacts") {
    group = "distribution"
    description = "Builds and collects all deployable fat jars into build/deployment."

    val shadowTasks = availableDeployableProjects.keys.map { path -> "$path:shadowJar" }
    dependsOn(shadowTasks)

    into(layout.buildDirectory.dir("deployment"))
    availableDeployableProjects.forEach { (path, jarName) ->
        val projectPath = path.removePrefix(":")
        from(layout.projectDirectory.file("$projectPath/build/libs/$jarName"))
    }
}
