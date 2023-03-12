import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    id("org.springframework.boot") version "2.4.4"
    id("io.spring.dependency-management") version "1.0.11.RELEASE"
    kotlin("jvm") version "1.5.30"
    kotlin("plugin.spring") version "1.5.30"
}
group = "stock-group"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    implementation(kotlin("stdlib"))
    implementation(project(":common"))
    implementation("org.liquibase:liquibase-core:3.10.3")
    implementation(group = "org.springframework.boot", name = "spring-boot-starter-data-r2dbc")
    implementation(group = "org.springframework.boot", name = "spring-boot-starter-web")
    implementation(group = "org.springframework.boot", name = "spring-boot-devtools")
    implementation(group = "org.springframework.boot", name = "spring-boot-starter-validation")

    implementation(group = "io.r2dbc", name =  "r2dbc-postgresql", version =  "0.8.7.RELEASE")
    implementation(group =  "org.postgresql", name =  "postgresql", version = "42.2.27")
    implementation("org.jetbrains.kotlin:kotlin-reflect:1.4.32")
    implementation(group =  "org.springframework", name =  "spring-jdbc", version =  "5.3.5")


    testImplementation("org.springframework.boot:spring-boot-starter-test")
}

tasks.withType<KotlinCompile> {
    kotlinOptions {
        freeCompilerArgs = listOf("-Xjsr305=strict")
        jvmTarget = "11"
    }
}

tasks.withType<Test> {
    useJUnitPlatform()
}