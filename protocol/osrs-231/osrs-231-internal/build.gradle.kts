dependencies {
    api(platform(rootProject.libs.netty.bom))
    api(rootProject.libs.netty.buffer)
    api(rootProject.libs.netty.transport)
    api(rootProject.libs.commons.pool2)
    implementation(rootProject.libs.inline.logger)
    api(projects.buffer)
    api(projects.compression)
    api(projects.crypto)
    api(projects.protocol)
    api(projects.protocol.osrs231.osrs231Common)
}

mavenPublishing {
    pom {
        name = "RsProt OSRS 231 Internal"
        description = "The internal module for revision 231 OldSchool RuneScape networking, " +
            "offering internal hidden implementations behind the library."
    }
}
