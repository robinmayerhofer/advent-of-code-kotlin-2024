import kotlinx.benchmark.gradle.JvmBenchmarkTarget
import org.jetbrains.kotlin.allopen.gradle.AllOpenExtension

plugins {
    kotlin("jvm") version "2.0.21"
    kotlin("plugin.allopen") version "2.0.20"
    id("org.jetbrains.kotlinx.benchmark") version "0.4.12"
//    id("org.graalvm.buildtools.native") version "0.10.3"
}

dependencies {
    implementation("org.jetbrains.kotlinx:kotlinx-benchmark-runtime:0.4.12")
}

configure<AllOpenExtension> {
    annotation("org.openjdk.jmh.annotations.State")
}

sourceSets {
    main {
        kotlin.srcDir("src")
    }
}

tasks {
    wrapper {
        gradleVersion = "8.11"
    }
}

benchmark {
    configurations {
        named("main") {
            iterationTime = 5
            iterationTimeUnit = "sec"

        }
        register("Day16Speed") {
            iterationTime = 5
            iterationTimeUnit = "sec"
            include("days.day16speed.Day16Benchmark")
        }
    }
    targets {
        register("main") {
            this as JvmBenchmarkTarget
            jmhVersion = "1.35"
        }
    }
}

// run with java -jar build/libs/advent-of-code-kotlin-2024-standalone.jar src/Day16.txt
tasks {
    val fatJar = register<Jar>("fatJar") {
        dependsOn.addAll(listOf("compileJava", "compileKotlin", "processResources")) // We need this for Gradle optimization to work
        archiveClassifier.set("standalone") // Naming the jar
        manifest {
            attributes("Main-Class" to "days.day22speed.Day22Speed")
        }
        duplicatesStrategy = DuplicatesStrategy.EXCLUDE
        val sourcesMain = sourceSets.main.get()
        val contents = configurations.runtimeClasspath.get()
            .map { if (it.isDirectory) it else zipTree(it) } +
                sourcesMain.output
        from(contents)
    }
    build {
        dependsOn(fatJar) // Trigger fat jar creation during build
    }
}

// build with ./gradlew nativeCompile (first set jenv version to the Graal VM)
//kotlin {
//    jvmToolchain(21)
//}
//
//graalvmNative {
//    toolchainDetection.set(true)
//    binaries {
//        named("main") {
//            mainClass.set("days.day16speed.Day16Speed")
//        }
//    }
//}
