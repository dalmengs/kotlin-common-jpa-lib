plugins {
    id("java-library")
    id("maven-publish")
    kotlin("jvm") version "1.9.25"
    kotlin("kapt") version "1.9.25"
    kotlin("plugin.jpa") version "1.9.25"
    kotlin("plugin.allopen") version "1.9.25"
}

group = "com.dalmeng"
version = "1.0.3"

repositories {
    mavenCentral()
}

dependencies {
    implementation("jakarta.persistence:jakarta.persistence-api:3.1.0")
    
    // ULID Generator
    implementation("com.github.f4b6a3:ulid-creator:5.2.3")

    // QueryDSL
    implementation("com.querydsl:querydsl-jpa:5.0.0:jakarta")
    kapt("com.querydsl:querydsl-apt:5.0.0:jakarta")
    kapt("jakarta.annotation:jakarta.annotation-api:2.1.1")
    kapt("jakarta.persistence:jakarta.persistence-api:3.1.0")
}

java {
    sourceCompatibility = JavaVersion.VERSION_17
    targetCompatibility = JavaVersion.VERSION_17
    withSourcesJar()
}

kotlin {
    jvmToolchain(17)
}

val generated = file("build/generated/source/kapt/main")

sourceSets {
    main {
        java.srcDirs += generated
    }
}

tasks.named("clean") {
    doLast {
        generated.deleteRecursively()
    }
}

allOpen {
    annotation("jakarta.persistence.Entity")
    annotation("jakarta.persistence.MappedSuperclass")
    annotation("jakarta.persistence.Embeddable")
}

publishing {
    publications {
        create<MavenPublication>("maven") {
            from(components["java"])
            
            groupId = project.group.toString()
            artifactId = project.name
            version = project.version.toString()
            
            pom {
                name.set("Kotlin Common JPA Library")
                description.set("Common JPA utilities for Spring Boot Kotlin applications including base entities, repositories, and QueryDSL support")
                url.set("https://github.com/dalmengs/kotlin-common-jpa-lib")
                
                licenses {
                    license {
                        name.set("The Apache License, Version 2.0")
                        url.set("http://www.apache.org/licenses/LICENSE-2.0.txt")
                    }
                }
                
                developers {
                    developer {
                        id.set("dalmeng")
                        name.set("dalmeng")
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

// QueryDSL APT 설정
kapt {
    correctErrorTypes = true
    arguments {
        arg("querydsl.entityAccessors", "true")
        arg("querydsl.useFields", "true")
        arg("querydsl.defaultEntityPackage", "com.dalmeng.common.jpa.entity")
    }
}

tasks.named("compileJava") {
    dependsOn("kaptKotlin")
}