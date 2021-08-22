plugins {
    kotlin("jvm")
}

dependencies {
    implementation(kotlin("stdlib"))

    implementation(project(":m3-common"))
    implementation(project(":m3-transport-main-openapi"))
}