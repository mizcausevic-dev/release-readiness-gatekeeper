plugins {
    kotlin("jvm") version "2.0.21"
    application
}

repositories {
    mavenCentral()
}

dependencies {
    implementation(kotlin("stdlib"))
    implementation("io.javalin:javalin:6.6.0")
    implementation("ch.qos.logback:logback-classic:1.5.18")
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin:2.19.0")

    testImplementation(kotlin("test"))
    testImplementation("io.javalin:javalin-testtools:6.6.0")
    testImplementation("org.junit.jupiter:junit-jupiter:5.11.4")
}

kotlin {
    jvmToolchain(21)
}

application {
    mainClass.set("com.mizcausevic.releasereadiness.MainKt")
}

tasks.test {
    useJUnitPlatform()
}
