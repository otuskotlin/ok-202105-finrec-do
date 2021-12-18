val ktorVersion: String by project
val kotestVersion: String by project

fun DependencyHandler.ktor(module: String, version: String? = ktorVersion): Any =
    "io.ktor:ktor-$module:$version"

fun DependencyHandler.kotest(module: String, version: String? = kotestVersion): Any =
    "io.kotest:kotest-$module:$version"



plugins {
    application
    kotlin("jvm")
    id("com.bmuschko.docker-java-application")
}

application {
    mainClass.set("io.ktor.server.netty.EngineMain")
}

docker {
    javaApplication {
        mainClassName.set(application.mainClass.get())
        baseImage.set("adoptopenjdk/openjdk11:alpine-jre")
        maintainer.set("(c) Otus")
        ports.set(listOf(8080))
        val imageName = project.name
        images.set(
            listOf(
                "$imageName:${project.version}",
                "$imageName:latest"
            )
        )
        jvmArgs.set(listOf("-Xms256m", "-Xmx512m"))
    }
}

dependencies {

    val logbackVersion: String by project

    implementation(kotlin("stdlib"))

    implementation(ktor("server-netty"))
    implementation(ktor("server-core"))
    implementation(ktor("jackson"))
    implementation(ktor("websockets"))
    implementation(ktor("auth"))
    implementation(ktor("auth-jwt"))

    testImplementation(ktor("server-tests"))
    testImplementation(ktor("server-test-host"))

    testImplementation(kotest("assertions-core"))
    testImplementation(kotest("property"))
    testImplementation(kotest("runner-junit5"))

    testImplementation(project(":m4-stubs"))

    implementation(project(":m3-common"))
    implementation(project(":m3-transport-main-openapi"))
    implementation(project(":m3-transport-mapping-openapi"))
    implementation(project(":m5-cor-logics"))
    implementation(project(":m7-repo-inmemory"))
    implementation(project(":m7-repo-postgresql"))
    implementation(project(":m9-logging"))
}

tasks.withType<Test> {
    useJUnitPlatform()
}