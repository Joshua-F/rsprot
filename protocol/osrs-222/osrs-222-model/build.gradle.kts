dependencies {
    api(platform(rootProject.libs.netty.bom))
    api(rootProject.libs.netty.buffer)
    implementation(rootProject.libs.inline.logger)
    api(rootProject.libs.commons.pool2)
    api(projects.buffer)
    api(projects.compression)
    api(projects.crypto)
    api(projects.protocol)
    api(projects.protocol.osrs222.osrs222Internal)
    api(projects.protocol.osrs222.osrs222Common)
}

mavenPublishing {
    pom {
        name = "RsProt OSRS 222 Model"
        description = "The model module for revision 222 OldSchool RuneScape networking, " +
            "offering all the model classes to be used by the implementing server."
    }
}
