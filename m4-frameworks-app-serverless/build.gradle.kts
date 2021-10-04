import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

val kotestVersion: String by project
val jacksonVersion: String by project

fun DependencyHandler.kotest(module: String, version: String? = kotestVersion): Any =
    "io.kotest:kotest-$module:$version"

fun DependencyHandler.aws(module: String, version: String): Any =
    "com.amazonaws:aws-lambda-java-$module:$version"

plugins {
    kotlin("jvm")
}

dependencies {
    implementation(kotlin("stdlib"))

    testImplementation(kotest("assertions-core"))
    testImplementation(kotest("property"))
    testImplementation(kotest("runner-junit5"))

    implementation(project(":m3-common"))
    implementation(project(":m3-transport-main-openapi"))
    implementation(project(":m3-transport-mapping-openapi"))
    implementation(project(":m4-stubs"))

    implementation(aws("core", "1.2.1"))
    implementation(aws("log4j", "1.0.1"))
    implementation(aws("events", "3.9.0"))

    implementation("com.fasterxml.jackson.core:jackson-core:$jacksonVersion")
    implementation("com.fasterxml.jackson.core:jackson-databind:$jacksonVersion")
    implementation("com.fasterxml.jackson.core:jackson-annotations:$jacksonVersion")
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin:$jacksonVersion")
}

tasks.withType<KotlinCompile> {
    kotlinOptions {
        freeCompilerArgs = listOf("-Xjsr305=strict")
        jvmTarget = "11"
    }
}

tasks.withType<Test> {
    useJUnitPlatform()
}

val buildZip by tasks.creating(Zip::class) {
    archiveBaseName.set("functions")
    from(tasks.named("compileKotlin"))
    from(tasks.named("processResources"))
    into("lib") {
        from(configurations.runtimeClasspath)
    }
}

val build by tasks.getting {
    dependsOn(buildZip)
}