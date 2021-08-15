plugins {
    kotlin("multiplatform")
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
    linuxX64 {
        binaries {
            executable {
                baseName = "fintrackApp"
            }
        }
    }

    val kotestVersion: String by project
    val coroutinesVersion: String by project

    sourceSets {
        val commonMain by getting {
            dependencies {
                implementation(kotlin("stdlib-common"))
                implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:$coroutinesVersion")
            }
        }
        val commonTest by getting {
            dependencies {
                implementation(kotlin("test-common"))
                implementation(kotlin("test-annotations-common"))
                implementation("io.kotest:kotest-assertions-core:$kotestVersion")
                implementation("io.kotest:kotest-property:$kotestVersion")
            }
        }
        val jsMain by getting {
            dependencies {
                implementation(kotlin("stdlib-js"))
            }
        }
        val jsTest by getting {
            dependencies {
                implementation(kotlin("test-js"))
                implementation("io.kotest:kotest-framework-engine:$kotestVersion")
            }
        }
        val jvmMain by getting {
            dependencies {
                implementation(kotlin("stdlib-jdk8"))
            }
        }
        val jvmTest by getting {
            dependencies {
                implementation(kotlin("test-junit5"))
                implementation("io.kotest:kotest-runner-junit5:$kotestVersion")
            }
        }
        val linuxX64Main by getting {
            dependencies {
                implementation(kotlin("stdlib"))
            }
        }
        val linuxX64Test by getting {
            dependencies {
                implementation(kotlin("test"))
            }
        }
        tasks.withType<Test>().configureEach {
            useJUnitPlatform()
        }
    }
}