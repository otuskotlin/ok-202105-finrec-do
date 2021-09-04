rootProject.name = "fintrack"

pluginManagement {
    val kotlinVersion: String by settings
    val openApiVersion: String by settings
    val springBootVersion: String by settings
    val springDependencyVersion: String by settings
    val springPluginVersion: String by settings

    plugins {
        kotlin("jvm") version kotlinVersion
        kotlin("multiplatform") version kotlinVersion
        kotlin("plugin.serialization") version kotlinVersion

        id("org.openapi.generator") version openApiVersion

        id("org.springframework.boot") version springBootVersion
        id("io.spring.dependency-management") version springDependencyVersion
        kotlin("plugin.spring") version springPluginVersion
    }
}

include("common")
include("m2l2-testing")
include("m3-common")
include("m3-transport-main-openapi")
include("m3-transport-mapping-openapi")
include("m3-transport-main-mp")
include("m3-transport-mapping-mp")
include("m4-frameworks-spring-app")
