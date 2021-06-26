plugins {
    kotlin("jvm") version "1.5.10"

    id("com.github.johnrengelman.shadow") version "6.1.0"

    `maven-publish`
}

group = "net.hyren"
version = "0.1-ALPHA"

repositories {
    mavenCentral()

    jcenter()

    maven("https://repository.hyren.net/") {
        credentials {
            username = System.getenv("MAVEN_USERNAME")
            password = System.getenv("MAVEN_PASSWORD")
        }
    }
}

dependencies {
    // kotlin
    compileOnly(kotlin("stdlib"))
    compileOnly(kotlin("reflect"))

    // paer-spigot-server
    compileOnly("org.github.paperspigot:paper-spigot-server:1.8.8-R0.1-SNAPSHOT")

    // exposed
    compileOnly("org.jetbrains.exposed:exposed-core:0.31.1")
    compileOnly("org.jetbrains.exposed:exposed-dao:0.31.1")
    compileOnly("org.jetbrains.exposed:exposed-jdbc:0.31.1")
    compileOnly("org.jetbrains.exposed:exposed-jodatime:0.31.1")

    // redis
    compileOnly("redis.clients:jedis:3.3.0")

    // jackson
    compileOnly("com.fasterxml.jackson.core:jackson-core:2.12.3")
    compileOnly("com.fasterxml.jackson.core:jackson-databind:2.12.3")
    compileOnly("com.fasterxml.jackson.core:jackson-annotations:2.12.3")

    // eventbus
    compileOnly("org.greenrobot:eventbus:3.2.0")

    // caffeine
    compileOnly("com.github.ben-manes.caffeine:caffeine:2.8.5")

    // factions-alpha
    compileOnly("net.hyren:factions-alpha:0.1-ALPHA")

    // core
    compileOnly("net.hyren:core-shared:0.1-ALPHA")
    compileOnly("net.hyren:core-spigot:0.1-ALPHA")
}

tasks {
    compileKotlin {
        kotlinOptions {
            jvmTarget = "1.8"
        }
    }

    shadowJar {
        archiveFileName.set("${project.name}.jar")
    }
}

configurations.all {
    resolutionStrategy.cacheChangingModulesFor(120, "seconds")
}

val sources by tasks.registering(Jar::class) {
    archiveFileName.set(project.name)
    archiveClassifier.set("sources")
    archiveVersion.set(null as String?)

    from(sourceSets.main.get().allSource)
}

publishing {
    publications {
        create<MavenPublication>("maven") {
            repositories {
                maven("https://repository.hyren.net/") {
                    isAllowInsecureProtocol = true

                    credentials {
                        username = System.getenv("MAVEN_USERNAME")
                        password = System.getenv("MAVEN_PASSWORD")
                    }
                }
            }

            from(components["kotlin"])
            artifact(sources.get())
        }
    }
}