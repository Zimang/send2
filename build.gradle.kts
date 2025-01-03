plugins {
    kotlin("jvm") version "2.0.21"
}

group = "org.example"
version = "1.0-SNAPSHOT"

repositories {
    maven (url="https://maven.aliyun.com/repository/grails-core/")
    maven (url="https://maven.aliyun.com/repository/google")
    maven (url="https://maven.aliyun.com/repository/gradle-plugin/")

//    mavenCentral()
}

dependencies {
    testImplementation(kotlin("test"))
    // JUnit 5
    testImplementation("org.junit.jupiter:junit-jupiter:5.10.0")

    // Kotlin test helpers
    testImplementation("org.jetbrains.kotlin:kotlin-test:1.9.10")
    implementation("org.apache.commons:commons-compress:1.27.1")
}

tasks.test {
    useJUnitPlatform()
}
kotlin {
    jvmToolchain(11)
}