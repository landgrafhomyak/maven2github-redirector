import org.jetbrains.kotlin.gradle.dsl.ExplicitApiMode

plugins {
    kotlin("multiplatform") version "1.9.0"
    java
    application
}

group = "ru.landgrafhomyak"
version = "0.1"

repositories {
    mavenCentral()
//    maven("http://localhost:8080/") { isAllowInsecureProtocol = true }
//    maven("https://maven.landgrafhomyak.ru/")
}

tasks.withType<JavaCompile> {
    options.encoding = "UTF-8"
    sourceCompatibility = "11"
    targetCompatibility = "11"
}

java {
    this.sourceCompatibility = JavaVersion.VERSION_11
    this.targetCompatibility = JavaVersion.VERSION_11
}

application {
    mainClass.set("ru.landgrafhomyak.maven2github_redirector.MainKt")
}


kotlin {
    explicitApi = ExplicitApiMode.Strict
    jvm {
        compilations.all {
            kotlinOptions.jvmTarget = "11"
        }

        tasks.register<Jar>("buildFatJar") {
            duplicatesStrategy = DuplicatesStrategy.EXCLUDE
            group = "application"
            // archiveVersion.set("v${project.version}")
            manifest {
                attributes["Main-Class"] = application.mainClass.get()
            }
            from(compilations["main"].output.classesDirs)
            from(compilations["main"].runtimeDependencyFiles.map { d -> if (d.isDirectory) d else zipTree(d) })
            with(tasks.jar.get() as CopySpec)
            destinationDirectory = projectDir.resolve("out")
        }

        withJava()
    }

    js(IR) {
        binaries.executable()
        browser {
            commonWebpackConfig {
                outputFileName = "loader.js"
                outputPath = rootDir.resolve("src/jvmMain/resources")
            }
        }
    }

    @Suppress("UNUSED_VARIABLE")
    sourceSets {
        val commonMain by getting {
            dependencies {
                implementation("org.xerial:sqlite-jdbc:3.40.0.0")
            }
        }
        val commonTest by getting {
            dependencies {
                implementation(kotlin("test"))
            }
        }
        val jvmMain by getting {
            dependsOn(commonMain)
            dependencies {
//                implementation("ru.landgrafhomyak.utility:java-resource-loader:1.0")
            }
        }
        val jsMain by getting {
            dependsOn(commonMain)
        }
    }
}