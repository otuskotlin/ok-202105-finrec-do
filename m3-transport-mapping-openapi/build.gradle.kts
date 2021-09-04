plugins {
    kotlin("jvm")
}

dependencies {
    val kotestVersion: String by project

    implementation(kotlin("stdlib"))

    implementation(project(":m3-common"))
    implementation(project(":m3-transport-main-openapi"))

    testImplementation("io.kotest:kotest-assertions-core:$kotestVersion")
    testImplementation("io.kotest:kotest-property:$kotestVersion")
    testImplementation ("io.kotest:kotest-runner-junit5:$kotestVersion")
}

tasks.withType<Test> {
    useJUnitPlatform()
}