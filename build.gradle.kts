
import com.github.vlsi.gradle.crlf.CrLfSpec
import com.github.vlsi.gradle.crlf.LineEndings
import com.github.vlsi.gradle.properties.dsl.props
import com.github.vlsi.gradle.properties.dsl.stringProperty
import com.github.vlsi.gradle.properties.dsl.toBool
import com.github.vlsi.gradle.publishing.dsl.simplifyXml
import com.github.vlsi.gradle.publishing.dsl.versionFromResolution

plugins {
    idea
    id("com.diffplug.spotless")
    id("com.github.vlsi.crlf")
    id("com.github.vlsi.gradle-extensions")
    id("com.github.vlsi.stage-vote-release")
}

val skipJavadoc by props()
val enableMavenLocal by props(false)
val enableGradleMetadata by props()
val skipSpotless by props(false)
val isRelease = project.stringProperty("release").toBool()
val snapshotName by props("")

if (isRelease && !JavaVersion.current().isJava9Compatible) {
    throw GradleException("Java 9 compatible compiler is needed for release builds")
}

val String.v: String get() = rootProject.extra["$this.version"] as String
val projectVersion = "image-comparison".v

val snapshotIdentifier = if (!isRelease && snapshotName.isNotEmpty()) "-$snapshotName" else ""

releaseParams {
    tlp.set("image-comparison")
    organizationName.set("weisJ")
    componentName.set("image-comparison")
    prefixForProperties.set("gh")
    svnDistEnabled.set(false)
    sitePreviewEnabled.set(false)
    release.set(isRelease)
    if (!isRelease) {
        rcTag.set("v$projectVersion$snapshotIdentifier$snapshotSuffix")
    }
    nexus {
        mavenCentral()
    }
    voteText.set {
        """
        ${it.componentName} v${it.version}-rc${it.rc} is ready for preview.

        Git SHA: ${it.gitSha}
        Staging repository: ${it.nexusRepositoryUri}
        """.trimIndent()
    }
}

tasks.closeRepository.configure { enabled = isRelease }

val buildVersion = "$projectVersion$snapshotIdentifier${releaseParams.snapshotSuffix}"
println("Building: image-comparison $buildVersion")
println("     JDK: " + System.getProperty("java.home"))
println("  Gradle: " + gradle.gradleVersion)

allprojects {
    group = "com.github.weisj"
    version = buildVersion

    repositories {
        if (enableMavenLocal) {
            mavenLocal()
        }
        if (!isRelease) {
            maven { url = uri("https://oss.sonatype.org/content/repositories/snapshots/") }
        }
        mavenCentral()
    }

    configurations.all {
        resolutionStrategy.cacheChangingModulesFor(0, "seconds")
    }

    if (!skipSpotless) {
        apply(plugin = "com.diffplug.spotless")
        spotless {
            val spotlessRatchet by props(default = true)
            if (spotlessRatchet) {
                ratchetFrom("origin/master")
            }
            kotlinGradle {
                ktlint("ktlint".v)
            }
            format("markdown") {
                target("**/*.md")
                endWithNewline()
                trimTrailingWhitespace()
            }
            plugins.withType<JavaPlugin>().configureEach {
                java {
                    importOrder("java", "javax", "org", "com")
                    removeUnusedImports()
                    eclipse().configFile("${project.rootDir}/config/java.eclipseformat.xml")
                    licenseHeaderFile("${project.rootDir}/config/LICENSE_HEADER.txt")
                }
            }
        }
    }

    tasks.withType<AbstractArchiveTask>().configureEach {
        // Ensure builds are reproducible
        isPreserveFileTimestamps = false
        isReproducibleFileOrder = true
        dirMode = "775".toInt(8)
        fileMode = "664".toInt(8)
    }

    if (!enableGradleMetadata) {
        tasks.withType<GenerateModuleMetadata> {
            enabled = false
        }
    }

    plugins.withType<JavaPlugin> {
        configure<JavaPluginExtension> {
            sourceCompatibility = JavaVersion.VERSION_1_8
            targetCompatibility = JavaVersion.VERSION_1_8
            withSourcesJar()
            if (!skipJavadoc && isRelease) {
                withJavadocJar()
            }
        }
        apply(plugin = "maven-publish")

        val useInMemoryKey by props()
        if (useInMemoryKey) {
            apply(plugin = "signing")

            configure<SigningExtension> {
                useInMemoryPgpKeys(
                    project.stringProperty("signing.inMemoryKey")?.replace("#", "\n"),
                    project.stringProperty("signing.password")
                )
            }
        }

        tasks {
            withType<JavaCompile>().configureEach {
                options.encoding = "UTF-8"
            }

            withType<ProcessResources>().configureEach {
                from(source) {
                    include("**/*.properties")
                    filteringCharset = "UTF-8"
                    duplicatesStrategy = DuplicatesStrategy.INCLUDE
                    // apply native2ascii conversion since Java 8 expects properties to have ascii symbols only
                    filter(org.apache.tools.ant.filters.EscapeUnicode::class)
                }
            }

            withType<Jar>().configureEach {
                manifest {
                    attributes["Bundle-License"] = "MIT"
                    attributes["Implementation-Title"] = project.name
                    attributes["Implementation-Version"] = project.version
                    attributes["Specification-Vendor"] = "image-comparison"
                    attributes["Specification-Version"] = project.version
                    attributes["Specification-Title"] = "image-comparison"
                    attributes["Implementation-Vendor"] = "image-comparison"
                    attributes["Implementation-Vendor-Id"] = "com.github.weisj"
                }

                CrLfSpec(LineEndings.LF).run {
                    into("META-INF") {
                        filteringCharset = "UTF-8"
                        duplicatesStrategy = DuplicatesStrategy.EXCLUDE
                        // This includes either project-specific license or a default one
                        if (file("$projectDir/LICENSE").exists()) {
                            textFrom("$projectDir/LICENSE")
                        } else {
                            textFrom("$rootDir/LICENSE")
                        }
                    }
                }
            }

            withType<Javadoc>().configureEach {
                (options as StandardJavadocDocletOptions).apply {
                    // -add-exports requires target 9
                    // The library is built with target=1.8, so add-exports
                    if (project.the<JavaPluginExtension>().targetCompatibility.isJava9Compatible) {
                        addStringOption("-add-exports", "java.desktop/sun.swing=ALL-UNNAMED")
                        addStringOption("-add-exports", "java.desktop/sun.awt=ALL-UNNAMED")
                        addStringOption("-add-exports", "java.desktop/com.sun.java.swing=ALL-UNNAMED")
                        addStringOption("-add-exports", "java.desktop/sun.awt.shell=ALL-UNNAMED")
                    }
                    quiet()
                    locale = "en"
                    docEncoding = "UTF-8"
                    charSet = "UTF-8"
                    encoding = "UTF-8"
                    docTitle = "image-comparison ${project.name} API"
                    windowTitle = "image-comparison ${project.name} API"
                    header = "<b>image-comparison</b>"
                    addBooleanOption("Xdoclint:none", true)
                    addStringOption("source", "8")
                    if (JavaVersion.current().isJava9Compatible) {
                        addBooleanOption("html5", true)
                        links("https://docs.oracle.com/javase/9/docs/api/")
                    } else {
                        links("https://docs.oracle.com/javase/8/docs/api/")
                    }
                }
            }
        }

        configure<PublishingExtension> {
            if (project.path == ":") {
                // Skip the root project
                return@configure
            }

            publications {
                create<MavenPublication>(project.name) {
                    artifactId = "${project.name}$snapshotIdentifier"
                    version = buildVersion
                    description = project.description
                    from(project.components["java"])
                }
                withType<MavenPublication> {
                    // Use the resolved versions in pom.xml
                    // Gradle might have different resolution rules, so we set the versions
                    // that were used in Gradle build/test.
                    versionFromResolution()
                    pom {
                        simplifyXml()

                        description.set(
                            project.description
                                ?: "Library for comparing images"
                        )
                        name.set(
                            (project.findProperty("artifact.name") as? String)
                                ?: project.name.replaceFirstChar { it.uppercase() }.replace("-", " ")
                        )
                        url.set("https://github.com/weisJ/image-comparison")
                        organization {
                            name.set("com.github.weisj")
                            url.set("https://github.com/weisj")
                        }
                        issueManagement {
                            system.set("GitHub")
                            url.set("https://github.com/weisJ/image-comparison/issues")
                        }
                        licenses {
                            license {
                                name.set("MIT")
                                url.set("https://github.com/weisj/image-comparison/blob/master/LICENSE")
                                distribution.set("repo")
                            }
                        }
                        scm {
                            url.set("https://github.com/weisJ/image-comparison")
                            connection.set("scm:git:git://github.com/weisJ/image-comparison.git")
                            developerConnection.set("scm:git:ssh://git@github.com:weisj/image-comparison.git")
                        }
                        developers {
                            developer {
                                name.set("Roman Beskrovnyi")
                            }
                            developer {
                                name.set("Jannis Weis")
                            }
                        }
                    }
                }
            }
        }
    }
}
