val ktorVersion: String by project
val kotestVersion: String by project

fun DependencyHandler.ktor(module: String, version: String? = ktorVersion): Any =
    "io.ktor:ktor-$module:$version"

fun DependencyHandler.kotest(module: String, version: String? = kotestVersion): Any =
    "io.kotest:kotest-$module:$version"



plugins {
    application
    kotlin("jvm")
}

application {
    mainClass.set("io.ktor.server.netty.EngineMain")
}

dependencies {

    val logbackVersion: String by project

    implementation(kotlin("stdlib"))

    implementation(ktor("server-netty"))
    implementation(ktor("server-core"))
    implementation(ktor("jackson"))

    implementation("ch.qos.logback:logback-classic:$logbackVersion")

    testImplementation(ktor("server-tests"))

    testImplementation(kotest("assertions-core"))
    testImplementation(kotest("property"))
    testImplementation(kotest("runner-junit5"))

    implementation(project(":m3-common"))
    implementation(project(":m3-transport-main-openapi"))
    implementation(project(":m3-transport-mapping-openapi"))
    implementation(project(":m4-stubs"))
    implementation(project(":m5-cor-logics"))
}

tasks.withType<Test> {
    useJUnitPlatform()
}