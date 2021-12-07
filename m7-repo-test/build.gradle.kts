val coroutinesVersion: String by project

plugins {
    kotlin("jvm")
}

dependencies {

    implementation(kotlin("stdlib"))

    implementation(project(":m3-common"))
    implementation(project(":m4-stubs"))

    api("org.jetbrains.kotlinx:kotlinx-coroutines-core:$coroutinesVersion")
    api(kotlin("test-junit"))
}
