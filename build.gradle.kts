plugins {
    idea
    application
    kotlin("jvm") version "2.1.10"
    kotlin("plugin.spring") version "2.1.10"
    id("org.springframework.boot") version "3.4.3"
    id("io.spring.dependency-management") version "1.1.7"
    id("com.ncorti.ktfmt.gradle") version "0.22.0"
}

group = "io.demo"

kotlin { jvmToolchain { (this as JavaToolchainSpec).languageVersion.set(JavaLanguageVersion.of(17)) } }

repositories { mavenCentral() }

ktfmt {
    kotlinLangStyle()
    maxWidth.set(120)
    manageTrailingCommas.set(false)
}

val integrationTest by sourceSets.creating

configurations[integrationTest.implementationConfigurationName].extendsFrom(configurations["implementation"])

dependencies {
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.jetbrains.kotlin:kotlin-reflect")
    implementation("org.springframework.boot:spring-boot-starter-jdbc")
    implementation("org.postgresql:postgresql:42.7.5")
    implementation("org.flywaydb:flyway-core:11.4.0")
    implementation("org.flywaydb:flyway-database-postgresql:11.6.0")

    implementation("org.springframework.cloud:spring-cloud-stream-binder-kafka:4.2.1")

    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation("org.jetbrains.kotlin:kotlin-test-junit5")
    testImplementation("com.nhaarman.mockitokotlin2:mockito-kotlin:2.2.0")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")

    val integrationTestImplementation by configurations.getting
    integrationTestImplementation(project)
    integrationTestImplementation("io.rest-assured:spring-mock-mvc:5.5.1")
    integrationTestImplementation("org.springframework.boot:spring-boot-starter-test")
    integrationTestImplementation("org.testcontainers:testcontainers:1.20.6")
    integrationTestImplementation("org.apache.kafka:kafka-clients:3.9.0")
    integrationTestImplementation("net.javacrumbs.json-unit:json-unit-assertj:4.1.0")
}

kotlin { compilerOptions { freeCompilerArgs.addAll("-Xjsr305=strict") } }

tasks.withType<Test> { useJUnitPlatform() }

val integrationTestTask =
    tasks.register<Test>("integrationTest") {
        description = "Runs integration tests."
        group = "verification"
        useJUnitPlatform()

        testClassesDirs = integrationTest.output.classesDirs
        classpath = configurations[integrationTest.runtimeClasspathConfigurationName] + integrationTest.output

        shouldRunAfter(tasks.test)
    }

tasks.check { dependsOn(integrationTestTask) }

idea { module { testSources.from(sourceSets["integrationTest"].allJava.srcDirs) } }

application { mainClass.set("io.demo.mslibrary.infrastructure.ApplicationKt") }

/** Installs a Git pre-commit hook if forced or if it doesn't exist. */
class KtfmtPrecommit {
    companion object {
        fun install(rootDir: String, force: Boolean = false) {
            if (!isGitRepository(rootDir)) {
                throw IllegalStateException(
                    "The .git directory does not exist in the root directory. " +
                        "Please initialize a Git repository using `git init` before installing this plugin.")
            }

            val hooksDir = File(rootDir, ".git/hooks")
            if (!hooksDir.exists()) {
                hooksDir.mkdirs()
            }
            val file = File(hooksDir, "pre-commit")

            val ktfmtPrecommit =
                """
        #!/bin/sh

        CHANGED_FILES="${'$'}(git --no-pager diff --name-status --no-color --cached | awk '${'$'}1 != "D" && $\NF ~ /\.kts?${'$'}/ { print $\NF }')"

        echo "Running task ktfmtFormat"

        ./gradlew ktfmtFormat

        echo "$\CHANGED_FILES" | while read -r file; do
            if [ -f $\file ]; then
                git add $\file
            fi
        done
        """
                    .trimIndent()
                    .replace("\\", "")

            if (force || !file.exists()) {
                file.writeText(ktfmtPrecommit)
                file.setExecutable(true)
            }
        }

        private fun isGitRepository(rootDir: String): Boolean {
            val gitDir = File(rootDir, ".git")
            return gitDir.exists()
        }
    }
}

KtfmtPrecommit.install(rootProject.rootDir.path)

tasks.register<InstallKtfmtPrecommit>("installKtfmtPrecommit") {
    description =
        """
    Install a Git pre-commit hook that executes the ktfmtFormat task. Replaces existing pre-commit hook.
    """
            .trimIndent()
    group = "formatting"
    rootDir = rootProject.rootDir.path
}

abstract class InstallKtfmtPrecommit : DefaultTask() {

    @Input lateinit var rootDir: String

    @TaskAction
    fun action() {
        KtfmtPrecommit.install(rootDir, true)
    }
}

tasks.register<InstallEmptyPrecommit>("installEmptyPrecommit") {
    description =
        """
    Force the installation of a Git pre-commit hook that is empty.
    """
            .trimIndent()
    group = "formatting"
    rootDir = rootProject.rootDir.path
}

abstract class InstallEmptyPrecommit : DefaultTask() {

    @Input lateinit var rootDir: String

    @TaskAction
    fun action() {
        val file = File(rootDir, ".git/hooks/pre-commit")

        val emptyPrecommit =
            """
      #!/bin/sh

      # Git pre-commit is empty on purpose.
      """
                .trimIndent()
                .replace("\\", "")

        file.writeText(emptyPrecommit)
        file.setExecutable(true)
    }
}
