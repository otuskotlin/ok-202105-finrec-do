plugins {
    kotlin("jvm") apply false
    kotlin("multiplatform") apply false
}

group = "com.finyou.fintrack"
version = "0.0.1"

subprojects {
    group = rootProject.group
    version = rootProject.version

    repositories {
        mavenCentral()
    }
}