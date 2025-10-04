plugins {
    java
    application
    id("org.openjfx.javafxplugin") version "0.0.13"
    id("com.github.johnrengelman.shadow") version "8.1.1"
    id("org.beryx.runtime") version "1.13.1"
}

group = "sb"
version = "1.0"

repositories {
    mavenCentral()
}

val junitVersion = "5.10.2"

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(17)
    }
}

tasks.withType<JavaCompile> {
    options.encoding = "UTF-8"
}

application {
    mainClass.set("sb.justwindow.DynamicIsland")
}

javafx {
    version = "17.0.6"
    modules = listOf("javafx.controls", "javafx.graphics")
}

dependencies {
    implementation ("org.yaml:snakeyaml:2.2")
    testImplementation("org.junit.jupiter:junit-jupiter-api:${junitVersion}")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:${junitVersion}")
    implementation("org.json:json:20240303")
    implementation("com.google.code.gson:gson:2.10.1")
    implementation("net.java.dev.jna:jna:5.13.0")
    implementation("net.java.dev.jna:jna-platform:5.13.0")
}

tasks.withType<Test> {
    useJUnitPlatform()
}

tasks.withType<Jar> {
    manifest {
        attributes["Main-Class"] = "sb.justwindow.DynamicIsland"
    }
}

tasks.shadowJar {
    archiveBaseName.set("JustWindow")
    archiveClassifier.set("")
    archiveVersion.set("1.0")
    mergeServiceFiles()
}