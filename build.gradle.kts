val kotlin_version: String by project
val logback_version: String by project
val exposed_version: String = "0.61.0"
val flyway_version: String = "11.7.0"
val postgres_version: String = "42.7.5"
val hikaricp_version: String = "6.3.0"
val graphql_kotlin_version: String = "8.5.0"
val koin_version: String = "4.0.3"
val jbcrypt_version: String = "0.4"
val auth0_jwt_version: String = "4.5.0"

plugins {
    kotlin("jvm") version "2.1.20"
    id("io.ktor.plugin") version "3.1.2"
    id("org.jmailen.kotlinter") version "5.0.1"
    id("com.github.ben-manes.versions") version "0.52.0"
}

group = "com.fugisawa.playlistsgql"
version = "0.0.1"

application {
    mainClass = "io.ktor.server.netty.EngineMain"

    val isDevelopment: Boolean = project.ext.has("development")
    applicationDefaultJvmArgs = listOf("-Dio.ktor.development=$isDevelopment")
}

kotlin {
    jvmToolchain(21)
    sourceSets.all {
        languageSettings.optIn("kotlin.ExperimentalStdlibApi")
    }
}

repositories {
    mavenCentral()
}

dependencies {
    implementation("io.ktor:ktor-server-config-yaml")
    implementation("io.ktor:ktor-server-core")
    implementation("io.ktor:ktor-server-core-jvm")
    implementation("io.ktor:ktor-server-cors")
    implementation("io.ktor:ktor-server-host-common")
    implementation("io.ktor:ktor-server-netty")
    implementation("io.ktor:ktor-server-status-pages")

    implementation(project.dependencies.platform("io.insert-koin:koin-bom:$koin_version"))
    implementation("io.insert-koin:koin-core")
    implementation("io.insert-koin:koin-ktor")
    implementation("io.insert-koin:koin-logger-slf4j")

    implementation("ch.qos.logback:logback-classic:$logback_version")
    implementation("com.zaxxer:HikariCP:$hikaricp_version")

    implementation("org.postgresql:postgresql:$postgres_version")

    implementation("org.jetbrains.exposed:exposed-core:$exposed_version")
    implementation("org.jetbrains.exposed:exposed-dao:$exposed_version")
    implementation("org.jetbrains.exposed:exposed-jdbc:$exposed_version")
    implementation("org.jetbrains.exposed:exposed-java-time:$exposed_version")

    implementation("org.flywaydb:flyway-core:$flyway_version")
    implementation("org.flywaydb:flyway-database-postgresql:$flyway_version")

    implementation("com.expediagroup:graphql-kotlin-ktor-server:$graphql_kotlin_version")
    implementation("org.mindrot:jbcrypt:$jbcrypt_version")

    implementation("io.ktor:ktor-server-auth")
    implementation("io.ktor:ktor-server-auth-jwt")
    implementation("com.auth0:java-jwt:$auth0_jwt_version")

    testImplementation("io.ktor:ktor-server-test-host")
    testImplementation("org.jetbrains.kotlin:kotlin-test-junit:$kotlin_version")
}
