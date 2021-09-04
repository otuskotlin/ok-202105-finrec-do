plugins {
    kotlin("multiplatform")
    kotlin("plugin.serialization")
    id("org.openapi.generator")
}

kotlin {
    js {
        nodejs()
        browser {
            testTask {
                useKarma {
                    useFirefox()
                }
            }
        }
    }
    jvm {}

    val serializationVersion: String by project
    val generatedSourcesDir = "$buildDir/generated"

    sourceSets {
        val commonMain by getting {
            kotlin.srcDirs("$generatedSourcesDir/src/commonMain/kotlin")
            dependencies {
                implementation(kotlin("stdlib-common"))
                implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:$serializationVersion")
            }
        }
        val commonTest by getting {
            dependencies {
                implementation(kotlin("test-common"))
                implementation(kotlin("test-annotations-common"))
            }
        }
        val jsMain by getting {
            dependencies {
                implementation(kotlin("stdlib-js"))
            }
        }
        val jsTest by getting {
            dependencies {
                implementation(kotlin("test"))
                implementation(kotlin("test-js"))
            }
        }
        val jvmMain by getting {
            dependencies {
                implementation(kotlin("stdlib"))
            }
        }
        val jvmTest by getting {
            dependencies {
                implementation(kotlin("test"))
                implementation(kotlin("test-junit"))
            }
        }
    }

    openApiGenerate {
        val openapiGroup = "${rootProject.group}.kmp"
        generatorName.set("kotlin")
        library.set("multiplatform")
        outputDir.set(generatedSourcesDir)
        packageName.set(openapiGroup)
        apiPackage.set("$openapiGroup.api")
        modelPackage.set("$openapiGroup.models")
        invokerPackage.set("$openapiGroup.invoker")
        inputSpec.set("$rootDir/specs/spec-fintrack-api.yaml")

        globalProperties.apply {
            put("models", "")
            put("modelDocs", "false")
        }

        configOptions.set(
            mapOf(
                "dateLibrary" to "string",
                "enumPropertyNaming" to "UPPERCASE"
            )
        )
    }


    tasks {
        compileKotlinMetadata  {
            dependsOn(openApiGenerate)
        }
    }
}