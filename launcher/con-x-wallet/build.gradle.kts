/*
 * Copyright (c) 2026. Fraunhofer-Gesellschaft zur Foerderung der angewandten Forschung e.V. (represented by Fraunhofer ISST)
 *
 * This program and the accompanying materials are made available under the
 * terms of the Apache License, Version 2.0 which is available at
 * https://www.apache.org/licenses/LICENSE-2.0.
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations
 * under the License.
 *
 * SPDX-License-Identifier: Apache-2.0
 */

import org.gradle.api.tasks.Exec
import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar

plugins {
    `java-library`
    id("application")
    alias(libs.plugins.shadow)
}

val edcVersion = project.property("con-x-edcVersion") as String

dependencies {
    runtimeOnly("org.eclipse.edc:console-monitor:${edcVersion}")
    runtimeOnly("org.eclipse.edc:identityhub-bom:${edcVersion}")
    runtimeOnly("org.eclipse.edc:issuerservice-bom:${edcVersion}")
    runtimeOnly("org.eclipse.edc:identityhub-feature-sql-bom:${edcVersion}")
    runtimeOnly("org.eclipse.edc:issuerservice-feature-sql-bom:${edcVersion}")
    runtimeOnly("org.eclipse.edc:vault-hashicorp:${edcVersion}")

    runtimeOnly(project(":extensions:super-user-seed-extension"))
    runtimeOnly(project(":extensions:con-x:dev-attestation"))
}

repositories { mavenCentral() }

application {
    mainClass.set("org.eclipse.edc.boot.system.runtime.BaseRuntime")
}

val releaseVersion = "$edcVersion-1"

tasks.shadowJar {
    mergeServiceFiles()
    archiveFileName.set("$releaseVersion.jar")
    duplicatesStrategy = DuplicatesStrategy.INCLUDE
    manifest { attributes["Main-Class"] = application.mainClass.get() }
}


val imageName = "wallet:${releaseVersion}"
val shadowJar = tasks.named<ShadowJar>("shadowJar")
val jarFileName = "$releaseVersion.jar"

tasks.register<Exec>("dockerize") {
    dependsOn(shadowJar)

    workingDir = project.projectDir

    doFirst {
        val dockerfile = project.projectDir.resolve("Dockerfile")
        require(dockerfile.exists()) { "Dockerfile missing in ${project.projectDir}" }

        val jarFile = project.layout.buildDirectory.file("libs/$jarFileName").get().asFile
        require(jarFile.exists()) { "Shadow-Jar build/libs/$jarFileName missing " +
                "– please make sure \"shadowJar\" gradle task is successful" }

        commandLine(
            "docker", "build",
            "--build-arg", "JAR_FILE=$jarFileName",
            "-t", imageName,
            "."
        )
    }
}