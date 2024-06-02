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

dependencies {
    implementation(project(":type.generic"))
    implementation(project(":service.protocol"))
    implementation(project(":commons"))
    implementation(project(":proxy.api"))
    implementation("com.github.Minestom:Minestom:19bb74e942") {
        exclude(group = "org.jboss.shrinkwrap.resolver", module = "shrinkwrap-resolver-depchain")
    }
}