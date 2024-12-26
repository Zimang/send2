plugins {
    kotlin("jvm") version "2.0.21"
}

group = "org.example"
version = "1.0-SNAPSHOT"

repositories {
    maven {
//            url 'http://maven.aliyun.com/nexus/content/groups/public'
        url 'http://maven.aliyun.com/nexus/content/groups/public/'
        name 'Google'
    }
    maven {
//            url 'http://maven.aliyun.com/nexus/content/groups/public'
        url 'https://maven.aliyun.com/repository/grails-core'
        name 'gc'
    }
//    mavenCentral()
}

dependencies {
    testImplementation(kotlin("test"))
}

tasks.test {
    useJUnitPlatform()
}
kotlin {
    jvmToolchain(11)
}