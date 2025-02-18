plugins {
    idea
    kotlin("jvm") version "2.1.10"
    kotlin("plugin.spring") version "2.1.10"
    id("org.springframework.boot") version "3.4.2"
    id("io.spring.dependency-management") version "1.1.7"
    application
}

group = "io.demo"

kotlin {
    jvmToolchain {
        (this as JavaToolchainSpec).languageVersion.set(JavaLanguageVersion.of(17))
    }
}

repositories {
    mavenCentral()
}

val integrationTest by sourceSets.creating

dependencies {
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.jetbrains.kotlin:kotlin-reflect")
    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation("org.jetbrains.kotlin:kotlin-test-junit5")
    testImplementation("com.nhaarman.mockitokotlin2:mockito-kotlin:2.2.0")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")

    val integrationTestImplementation by configurations.getting
    integrationTestImplementation(project)
    integrationTestImplementation("io.rest-assured:spring-mock-mvc:5.5.0")
    integrationTestImplementation("org.springframework.boot:spring-boot-starter-test")
}

kotlin {
    compilerOptions {
        freeCompilerArgs.addAll("-Xjsr305=strict")
    }
}

tasks.withType<Test> {
    useJUnitPlatform()
}

val integrationTestTask = tasks.register<Test>("integrationTest") {
    description = "Runs integration tests."
    group = "verification"
    useJUnitPlatform()

    testClassesDirs = integrationTest.output.classesDirs
    classpath = configurations[integrationTest.runtimeClasspathConfigurationName] + integrationTest.output

    shouldRunAfter(tasks.test)
}

tasks.check {
    dependsOn(integrationTestTask)
}

idea {
    module {
        testSources.from(sourceSets["integrationTest"].allJava.srcDirs)
    }
}

application {
    mainClass.set("io.demo.mslibrary.infrastructure.ApplicationKt")
}