/*
 *  Copyright (c) 2022 Microsoft Corporation
 *
 *  This program and the accompanying materials are made available under the
 *  terms of the Apache License, Version 2.0 which is available at
 *  https://www.apache.org/licenses/LICENSE-2.0
 *
 *  SPDX-License-Identifier: Apache-2.0
 *
 *  Contributors:
 *       Amadeus - initial API and implementation
 *
 */

plugins {
    `java-library`
    id("application")
    alias(libs.plugins.shadow)
}

dependencies {
    val edcVersion = libs.versions.edc.get()

    runtimeOnly("org.eclipse.edc:identityhub-bom:$edcVersion")
    runtimeOnly("org.eclipse.edc:identityhub-feature-sql-bom:$edcVersion")
    runtimeOnly(libs.edc.vault.hashicorp)
    implementation(libs.edc.lib.common.crypto)
    implementation(project(":extensions:construct-x:super-user"))
}

application {
    mainClass.set("org.eclipse.edc.boot.system.runtime.BaseRuntime")
}

tasks.shadowJar {
    mergeServiceFiles()
    archiveFileName.set("identity-hub-cx.jar")
    duplicatesStrategy = DuplicatesStrategy.INCLUDE
}

edcBuild {
    publish.set(false)
}
