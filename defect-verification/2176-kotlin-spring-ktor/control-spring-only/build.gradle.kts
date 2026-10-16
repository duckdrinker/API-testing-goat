plugins { kotlin("jvm") version "2.0.21" }
repositories { mavenCentral() }
dependencies {
    implementation("io.ktor:ktor-server-core:2.3.12")
    implementation("io.ktor:ktor-server-netty:2.3.12")
    implementation("io.ktor:ktor-server-auth:2.3.12")
    implementation("io.ktor:ktor-server-auth-jwt:2.3.12")
    implementation("org.springframework.boot:spring-boot-starter-web:3.3.4")
}
