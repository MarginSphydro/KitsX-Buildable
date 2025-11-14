plugins {
    `java-library`
    id("com.github.johnrengelman.shadow") version "7.1.2"
    `maven-publish`
}

group = "dev.darkxx"
version = "1.0.3"
description = "KitsX"
java.sourceCompatibility = JavaVersion.VERSION_17
java.targetCompatibility = JavaVersion.VERSION_17

repositories {
    mavenCentral()
    mavenLocal()
    maven("https://repo.papermc.io/repository/maven-public/")
    maven("https://repo.skriptlang.org/releases")
    maven("https://maven.enginehub.org/repo/")
    maven("https://repo.extendedclip.com/content/repositories/placeholderapi/")
    maven("https://repo.extendedclip.com/content/repositories/public/")
}

dependencies {
    implementation("org.projectlombok:lombok:1.18.30")

    compileOnly("io.papermc.paper:paper-api:1.20.4-R0.1-SNAPSHOT")
    compileOnly("com.github.SkriptLang:Skript:dev37c")
    compileOnly("org.ow2.asm:asm:9.6")

    compileOnly("com.sk89q.worldedit:worldedit-bukkit:7.2.9")
    compileOnly("com.sk89q.worldguard:worldguard-bukkit:7.0.9")
    compileOnly("com.fastasyncworldedit:FastAsyncWorldEdit-Core:2.0.0") { isTransitive = false }
    compileOnly("me.clip:placeholderapi:2.11.5")
    annotationProcessor("org.projectlombok:lombok:1.18.30")
}

tasks.build {
    dependsOn(tasks.shadowJar)
}

tasks.withType<JavaCompile> {
    options.encoding = "UTF-8"
    options.compilerArgs.add("-parameters")
}

tasks.withType<Javadoc> {
    options.encoding = "UTF-8"
}

publishing {
    publications {
        create<MavenPublication>("mavenJava") {
            from(components["java"])
            groupId = "dev.darkxx"
            artifactId = "KitsX"
            version = "1.0.2"
        }
    }
    repositories {
        maven {
            url = uri("file://${System.getProperty("user.home")}/.m2/repository")
        }
    }
}
