plugins {
	kotlin("jvm") version "2.2.21"
	kotlin("plugin.spring") version "2.2.21"
    kotlin("plugin.allopen") version "2.2.21"
    kotlin("plugin.jpa") version "2.2.21"
	id("org.springframework.boot") version "4.0.2"
	id("io.spring.dependency-management") version "1.1.7"
}

group = "nl.maxbevelander"
version = "0.0.1-SNAPSHOT"
description = "telemax"

java {
	toolchain {
		languageVersion = JavaLanguageVersion.of(24)
	}
}

repositories {
	mavenCentral()
}

dependencies {
    // SPRING BOOT
	implementation("org.springframework.boot:spring-boot-starter-security")
	implementation("org.springframework.boot:spring-boot-starter-webmvc")
    implementation("org.springframework.boot:spring-boot-starter-thymeleaf")
    developmentOnly("org.springframework.boot:spring-boot-devtools")

    // KOTLIN
	implementation("org.jetbrains.kotlin:kotlin-reflect")

    // JACKSON
	implementation("tools.jackson.module:jackson-module-kotlin")

    // DATABASE
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")
    runtimeOnly("org.xerial:sqlite-jdbc:3.49.1.0")
    implementation("org.hibernate.orm:hibernate-community-dialects")

    // FLYWAY
    implementation("org.springframework.boot:spring-boot-starter-flyway")

    /* TEST DEPENDENCIES */
	testImplementation("org.springframework.boot:spring-boot-starter-security-test")
	testImplementation("org.springframework.boot:spring-boot-starter-webmvc-test")
	testImplementation("org.jetbrains.kotlin:kotlin-test-junit5")
	testRuntimeOnly("org.junit.platform:junit-platform-launcher")
}

kotlin {
	compilerOptions {
		freeCompilerArgs.addAll("-Xjsr305=strict", "-Xannotation-default-target=param-property")
	}
}

tasks.withType<Test> {
	useJUnitPlatform()
}
