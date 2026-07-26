plugins {
    id("java")
}

group = "org.example"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    testImplementation(platform("org.junit:junit-bom:5.10.0"))
    testImplementation("org.junit.jupiter:junit-jupiter")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
    // Source: https://mvnrepository.com/artifact/com.fasterxml.jackson.core/jackson-core
    implementation("com.fasterxml.jackson.core:jackson-core:2.20.1")
}

/// Конфигурация встроенной задачи test – запуск всех тестов
tasks.test {
    useJUnitPlatform()
    outputs.upToDateWhen { false }   // всегда считать задачу устаревшей
}

// Задача 1: запуск всех тестов (синоним test)
tasks.register("smokeTest") {
    dependsOn(tasks.test)
    group = "test"
    description = "Runs all tests"
}

// Задача 2: после прогона всех тестов выводит сообщение
tasks.register("finishedTest") {
    dependsOn("smokeTest")
    group = "test"
    doLast {
        println("Test run is over")
    }
}





/*tasks.register("SimpleTask") {
    group = "build"
    println("Simple task is running!")
}

tasks.named("SimpleTask") {
    dependsOn("Clean")
    dependsOn("anotherSimpleTask")
}

tasks.register("anotherSimpleTask") {
        doLast {
            println("Last string")
        }
        println("Env is setted!")
    }
*/

/*tasks.register<Test>("smoke"){
    group = "tests"
    systemProperty("CIRCUIT", System.getProperty("circuit", "DEV"))
    useJUnitPlatform{
        includeTags("Smoke")
    }
}
*/