import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
  id("org.springframework.boot") version "3.4.0"
  id("io.spring.dependency-management") version "1.1.3"
  kotlin("jvm") version "2.1.0"
  kotlin("kapt") version "2.1.0"
  kotlin("plugin.spring") version "2.1.0"
  kotlin("plugin.jpa") version "2.1.0"
}

group = "com.example"
version = "0.0.1-SNAPSHOT"

java {
  sourceCompatibility = JavaVersion.VERSION_21
  targetCompatibility = JavaVersion.VERSION_21
}

repositories {
  mavenCentral()
}

dependencies {
  implementation("org.springframework.boot:spring-boot-starter-web")

  // kotlin
  implementation("org.jetbrains.kotlin:kotlin-stdlib")
  implementation(kotlin("stdlib-jdk8"))
  implementation("org.jetbrains.kotlin:kotlin-reflect")

  // security
  implementation("org.springframework.boot:spring-boot-starter-security")

  // batch
  implementation("org.springframework.boot:spring-boot-starter-batch")

  // devtools
  developmentOnly("org.springframework.boot:spring-boot-devtools")

  // jpa
  implementation("org.springframework.boot:spring-boot-starter-data-jpa")

  // flyway
  implementation("org.flywaydb:flyway-core")

  // querydsl
  implementation("com.querydsl:querydsl-jpa:5.1.0:jakarta")
  implementation("com.querydsl:querydsl-apt:5.1.0:jakarta")
  kapt("com.querydsl:querydsl-apt:5.1.0:jakarta")
  kapt("jakarta.annotation:jakarta.annotation-api")
  kapt("jakarta.persistence:jakarta.persistence-api")

  // validation
  implementation("org.springframework.boot:spring-boot-starter-validation")

  // postgresql
  runtimeOnly("org.postgresql:postgresql")

  // jwt
  implementation("io.jsonwebtoken:jjwt:0.9.1")
  implementation("com.sun.xml.bind:jaxb-impl:4.0.1")
  implementation("com.sun.xml.bind:jaxb-core:4.0.1")
  implementation("javax.xml.bind:jaxb-api:2.4.0-b180830.0359")

  // redis
  implementation("org.springframework.boot:spring-boot-starter-data-redis")

  // swagger
  implementation("org.springdoc:springdoc-openapi-starter-webmvc-ui:2.7.0")

  // sentry
  implementation("io.sentry:sentry-spring-boot-starter-jakarta:7.9.0")
  implementation("io.sentry:sentry-logback:7.9.0")

  // h2
  runtimeOnly("com.h2database:h2")

  // logger
  implementation("io.github.oshai:kotlin-logging-jvm:7.0.0")

  // jackson
  implementation("com.fasterxml.jackson.module:jackson-module-kotlin")

  // test
  testImplementation("org.springframework.boot:spring-boot-starter-test")
  testImplementation("org.springframework.security:spring-security-test")
  testImplementation("org.springframework.batch:spring-batch-test")
  testImplementation("org.instancio:instancio-junit:5.0.0")

  // test - mockito
  testImplementation("org.mockito.kotlin:mockito-kotlin:5.4.0")

  // test - kotest & mockk
  testImplementation("io.kotest.extensions:kotest-extensions-spring:1.3.0")
  testImplementation("io.kotest:kotest-runner-junit5:5.9.1")
  testImplementation("io.kotest:kotest-assertions-core:5.9.1")
  testImplementation("io.mockk:mockk:1.13.13")
  testImplementation("com.ninja-squad:springmockk:4.0.2")
}

tasks.withType<KotlinCompile> {
  kotlin {
    compilerOptions {
      jvmTarget.set(JvmTarget.JVM_21)
      freeCompilerArgs.add("-Xjsr305=strict")
      languageVersion.set(org.jetbrains.kotlin.gradle.dsl.KotlinVersion.KOTLIN_2_1)
      apiVersion.set(org.jetbrains.kotlin.gradle.dsl.KotlinVersion.KOTLIN_2_1)
    }
    jvmToolchain(JvmTarget.JVM_21.target.toInt())
  }
}

tasks.withType<Test>().configureEach() {
  useJUnitPlatform()
}
