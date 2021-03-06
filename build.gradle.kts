import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import org.jetbrains.kotlin.kapt3.base.Kapt.kapt

plugins {
    id("org.springframework.boot") version "2.5.3"
    id("io.spring.dependency-management") version "1.0.11.RELEASE"
    id("org.jlleitschuh.gradle.ktlint") version "9.1.0"
    kotlin("jvm") version "1.5.21"
    kotlin("plugin.spring") version "1.5.21"
    kotlin("plugin.jpa") version "1.5.21"
    kotlin("kapt") version "1.5.21"
}

apply {
    plugin("org.jetbrains.kotlin.plugin.noarg")
    plugin("org.jetbrains.kotlin.plugin.allopen")
}

group = "com.zest"
version = "0.0.1-SNAPSHOT"
java.sourceCompatibility = JavaVersion.VERSION_14

configurations {
    compileOnly {
        extendsFrom(configurations.annotationProcessor.get())
    }
}
allOpen {
    annotation("javax.persistence.Entity")
    annotation("javax.persistence.MappedSuperclass")
    annotation("javax.persistence.Embeddable")
}
repositories {
    mavenCentral()
    maven(url = "https://plugins.gradle.org/m2/")
}

dependencies {

    implementation("org.springframework.boot:spring-boot-starter-data-jpa")
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.springframework.boot:spring-boot-starter-security")
    implementation("org.springframework.boot:spring-boot-starter-validation")
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
    implementation("org.jetbrains.kotlin:kotlin-reflect")
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")

    implementation("io.springfox:springfox-boot-starter:3.0.0")

    implementation("io.jsonwebtoken:jjwt:0.9.1")

    implementation("com.github.ulisesbocchio:jasypt-spring-boot-starter:3.0.3")

    implementation("com.querydsl:querydsl-jpa")//1
    kapt(group = "com.querydsl", name = "querydsl-apt", classifier = "jpa") // 2)

    // 3)
    sourceSets.main {
        withConvention(org.jetbrains.kotlin.gradle.plugin.KotlinSourceSet::class) {
            kotlin.srcDir("$buildDir/generated/source/kapt/main")
        }
    }

    implementation(group = "org.modelmapper", name = "modelmapper", version = "2.3.6")

    // https://mvnrepository.com/artifact/org.jlleitschuh.gradle/ktlint-gradle
    implementation("org.jlleitschuh.gradle:ktlint-gradle:9.1.0")


//    implementation("com.github.gavlyukovskiy:p6spy-spring-boot-starter:1.7.1")

    runtimeOnly("mysql:mysql-connector-java")
    runtimeOnly("com.h2database:h2")
    compileOnly("org.projectlombok:lombok")
    annotationProcessor("org.projectlombok:lombok")
    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation("org.springframework.security:spring-security-test")

}

tasks.withType<KotlinCompile> {
    kotlinOptions {
        freeCompilerArgs = listOf("-Xjsr305=strict")
        jvmTarget = "14"
    }
}

tasks.withType<Test> {
    useJUnitPlatform()
}
