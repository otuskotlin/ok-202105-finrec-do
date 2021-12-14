plugins {
    kotlin("jvm")
}

dependencies {

    val exposedVersion: String by project
    val postgresDriverVersion: String by project
    val testContainersVersion: String by project

    implementation(kotlin("stdlib"))

    implementation(project(":m3-common"))

    implementation("org.postgresql:postgresql:$postgresDriverVersion")

    implementation("org.jetbrains.exposed:exposed-core:$exposedVersion")
    implementation("org.jetbrains.exposed:exposed-dao:$exposedVersion")
    implementation("org.jetbrains.exposed:exposed-jdbc:$exposedVersion")

    testImplementation("org.testcontainers:postgresql:$testContainersVersion")

    testImplementation(project(":m7-repo-test"))
}

tasks {
    withType<Test> {
        environment("com.finyou.sql_drop_db", true)
        environment("com.finyou.sql_fast_migration", false)
    }
}
