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
//    maven("http://localhost:8080/")
//    maven("maven.landgrafhomyak.ru")
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
//                implementation("org.json:json:20220924")
//                implementation("ru.landgrafhomyak:json:10")
            }
        }
        val commonTest by getting {
            dependencies {
                implementation(kotlin("test"))
            }
        }
        val jvmMain by getting {
            dependsOn(commonMain)
        }
        val jsMain by getting {
            dependsOn(commonMain)
        }
    }
}