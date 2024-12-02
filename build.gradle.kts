import kotlinx.benchmark.gradle.JvmBenchmarkTarget
import org.jetbrains.kotlin.allopen.gradle.AllOpenExtension

plugins {
    kotlin("jvm") version "2.0.21"
    kotlin("plugin.allopen") version "2.0.20"
    id("org.jetbrains.kotlinx.benchmark") version "0.4.12"
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
        register("Day01") {
            iterationTime = 5
            iterationTimeUnit = "sec"
            include("days.day01.Day01Benchmark")
        }
    }
    targets {
        register("main") {
            this as JvmBenchmarkTarget
            jmhVersion = "1.35"
        }
    }
}
