rootProject.name = "fintrack"

pluginManagement {
    val kotlinVersion: String by settings
    val openApiVersion: String by settings
    val springBootVersion: String by settings
    val springDependencyVersion: String by settings
    val springPluginVersion: String by settings
    val bmuschkoVersion: String by settings

    plugins {
        kotlin("jvm") version kotlinVersion
        kotlin("multiplatform") version kotlinVersion
        kotlin("plugin.serialization") version kotlinVersion

        id("org.openapi.generator") version openApiVersion

        id("org.springframework.boot") version springBootVersion
        id("io.spring.dependency-management") version springDependencyVersion
        kotlin("plugin.spring") version springPluginVersion

        kotlin("plugin.allopen") version kotlinVersion
        id("com.bmuschko.docker-java-application") version bmuschkoVersion
    }
}

include("common")
include("m2l2-testing")
include("m3-common")
include("m3-transport-main-openapi")
include("m3-transport-mapping-openapi")
include("m3-transport-main-mp")
include("m3-transport-mapping-mp")
include("m4-frameworks-app-spring")
include("m4-stubs")
include("m4-frameworks-app-ktor")
include("m4-frameworks-app-serverless")
include("m5-cor-common")
include("m5-cor-logics")
include("m5-validation")
include("m7-repo-test")
include("m7-repo-inmemory")
include("m7-repo-postgresql")
include("m9-logging")
