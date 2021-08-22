rootProject.name = "fintrack"

pluginManagement {
    val kotlinVersion: String by settings
    val openApiVersion: String by settings

    plugins {
        kotlin("jvm") version kotlinVersion
        kotlin("multiplatform") version kotlinVersion

        id("org.openapi.generator") version openApiVersion
    }
}

include("common")
include("m2l2-testing")
include("m3-transport-main-openapi")
include("m3-common")
include("m3-transport-mapping-openapi")
