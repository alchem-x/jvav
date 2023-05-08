plugins {
    application
    id("org.openjfx.javafxplugin") version "0.0.13"
}

group = "jvav"

java.sourceCompatibility = JavaVersion.VERSION_20
java.targetCompatibility = JavaVersion.VERSION_20

javafx {
    version = "20"
    modules = listOf("javafx.web")
}

repositories {
    mavenCentral()
}

dependencies {
    implementation("org.jetbrains:annotations:24.0.1")
    testImplementation("org.junit.jupiter:junit-jupiter:5.9.3")
}

tasks.withType<Jar> {
    manifest {
        attributes["Main-Class"] = "jvav.App"
    }
}

application {
    mainClass.set("jvav.App")
    mainModule.set("jvav")
}

tasks.withType<Test> {
    useJUnitPlatform()
}
