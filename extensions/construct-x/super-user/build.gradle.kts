plugins {
    `java-library`
}

dependencies {
    implementation(libs.edc.ih.spi)
    implementation(libs.edc.spi.participantcontext.config)

    testImplementation(libs.edc.junit)
}