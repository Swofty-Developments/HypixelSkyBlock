plugins {
    id("java")
    id("maven-publish")
}

group = "net.swofty"
version = "1.0"

repositories {
    mavenCentral()

    maven {
        name = "GitHubPackages"
        url = uri("https://maven.pkg.github.com/Swofty-Developments/HypixelSkyBlock")
        credentials {
            username = project.findProperty("gpr.user") as String? ?: System.getenv("GITHUB_USERNAME")
            password = project.findProperty("gpr.key") as String? ?: System.getenv("GITHUB_TOKEN")
        }
    }
}

dependencies {
    implementation("com.github.Minestom:Minestom:2be6f9c507") {
        exclude(group = "org.jboss.shrinkwrap.resolver", module = "shrinkwrap-resolver-depchain")
    }
}

tasks.test {
    useJUnitPlatform()
}


java {
    sourceCompatibility = JavaVersion.VERSION_17
    targetCompatibility = JavaVersion.VERSION_17
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(21))
    }
}

publishing {
    repositories {
        maven {
            name = "GitHubPackages"
            url = uri("https://maven.pkg.github.com/Swofty-Developments/HypixelSkyBlock")
            credentials {
                username = project.findProperty("gpr.user") as String? ?: System.getenv("GITHUB_USERNAME")
                password = project.findProperty("gpr.key") as String? ?: System.getenv("GITHUB_TOKEN")
            }
        }
    }
    publications {
        register<MavenPublication>("gpr") {
            groupId = "net.swofty"
            artifactId = "anticheat"
            version = "1.0"

            from(components["java"])
        }
    }
}