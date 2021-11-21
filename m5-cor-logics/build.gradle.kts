plugins {
    kotlin("jvm")
}

dependencies {
    val kotestVersion: String by project
    val coroutinesVersion: String by project

    implementation(kotlin("stdlib"))

    implementation(project(":m3-common"))
    implementation(project(":m4-stubs"))
    implementation(project(":m5-cor-common"))

    testImplementation("io.kotest:kotest-assertions-core:$kotestVersion")
    testImplementation("io.kotest:kotest-property:$kotestVersion")
    testImplementation ("io.kotest:kotest-runner-junit5:$kotestVersion")
    testImplementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:$coroutinesVersion")
}

tasks.withType<Test> {
    useJUnitPlatform()
}
