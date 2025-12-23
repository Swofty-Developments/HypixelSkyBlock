plugins {
    java
}

group = "net.swofty"
version = "3.0"

java {
    sourceCompatibility = JavaVersion.VERSION_25
    targetCompatibility = JavaVersion.VERSION_25
}

dependencies {
    testImplementation("junit:junit:4.13.2")
    compileOnly("org.projectlombok:lombok:1.18.42")
}