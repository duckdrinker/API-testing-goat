plugins { kotlin("jvm") version "2.0.21"; kotlin("plugin.spring") version "2.0.21"; kotlin("plugin.jpa") version "2.0.21" }
repositories { mavenCentral() }
dependencies {
    implementation("org.springframework.boot:spring-boot-starter-web:3.3.4")
    implementation("org.springframework.boot:spring-boot-starter-data-jpa:3.3.4")
}
