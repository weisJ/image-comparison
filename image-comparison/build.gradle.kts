import org.gradle.api.tasks.testing.logging.TestExceptionFormat

plugins {
    `java-library`
}

dependencies {
    compileOnly(libs.nullabilityAnnotations)

    testImplementation(testLibs.mockito)
    testImplementation(testLibs.junit.api)
    testRuntimeOnly(testLibs.junit.engine)
    testCompileOnly(libs.nullabilityAnnotations)
}
tasks {

    compileTestJava {
        sourceCompatibility = JavaVersion.VERSION_17.toString()
        targetCompatibility = JavaVersion.VERSION_17.toString()
    }

    test {
        useJUnitPlatform()
        testLogging {
            showStandardStreams = true
            showExceptions = true
            showStackTraces = true
            exceptionFormat = TestExceptionFormat.FULL
        }
    }
}
