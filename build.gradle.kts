plugins {
    id("java-library")
    id("maven-publish")
    kotlin("jvm") version "1.9.22"
}

group = "com.dalmeng"
version = "1.0.0"

repositories {
    mavenCentral()
}

dependencies {
    // Jakarta Persistence API
    implementation("jakarta.persistence:jakarta.persistence-api:3.1.0")
    
    // ULID Generator
    implementation("com.github.f4b6a3:ulid-creator:5.2.3")
    
    // Kotlin Standard Library
    implementation("org.jetbrains.kotlin:kotlin-stdlib")
}

java {
    sourceCompatibility = JavaVersion.VERSION_17
    targetCompatibility = JavaVersion.VERSION_17
    withSourcesJar()
    withJavadocJar()
}

kotlin {
    jvmToolchain(17)
}

publishing {
    publications {
        create<MavenPublication>("maven") {
            from(components["java"])
            
            pom {
                name.set("Kotlin Common JPA Library")
                description.set("Common JPA base entity for Kotlin projects with ULID support and soft deletion")
                url.set("https://github.com/dalmengs/kotlin-common-jpa-lib")
                
                licenses {
                    license {
                        name.set("The Apache License, Version 2.0")
                        url.set("http://www.apache.org/licenses/LICENSE-2.0.txt")
                    }
                }
                
                developers {
                    developer {
                        id.set("dalmengs")
                        name.set("dalmengs")
                    }
                }
                
                scm {
                    connection.set("scm:git:git://github.com/dalmengs/kotlin-common-jpa-lib.git")
                    developerConnection.set("scm:git:ssh://github.com/dalmengs/kotlin-common-jpa-lib.git")
                    url.set("https://github.com/dalmengs/kotlin-common-jpa-lib")
                }
            }
        }
    }
    
    repositories {
        maven {
            name = "GitHubPackages"
            url = uri("https://maven.pkg.github.com/dalmengs/kotlin-common-jpa-lib")
            credentials {
                username = project.findProperty("gpr.user") as String? ?: System.getenv("GITHUB_ACTOR")
                password = project.findProperty("gpr.token") as String? ?: System.getenv("GITHUB_TOKEN")
            }
        }
    }
}

tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile> {
    kotlinOptions {
        freeCompilerArgs = listOf("-Xjsr305=strict")
        jvmTarget = "17"
    }
}
