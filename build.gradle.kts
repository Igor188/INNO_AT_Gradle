plugins {
    id("java")
    id("io.qameta.allure") version "3.2.0"
}

allure {
    version.set("2.29.0")
}

group = "org.example"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}


java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(21))   // ← JDK 21
    }
}

dependencies {
    testImplementation(platform("org.junit:junit-bom:5.10.0"))
    testImplementation("org.junit.jupiter:junit-jupiter")
    testImplementation("io.qameta.allure:allure-junit5:2.29.0")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
    // Source: https://mvnrepository.com/artifact/com.fasterxml.jackson.core/jackson-core
    implementation("com.fasterxml.jackson.core:jackson-core:2.20.1")
    // Source: https://mvnrepository.com/artifact/org.assertj/assertj-core
    testImplementation("org.assertj:assertj-core:3.27.7")
    // Source: https://mvnrepository.com/artifact/org.awaitility/awaitility
    testImplementation("org.awaitility:awaitility:4.3.0")
    // Source: https://mvnrepository.com/artifact/io.rest-assured/rest-assured
    implementation("io.rest-assured:rest-assured:5.5.6")
    // Source: https://mvnrepository.com/artifact/com.fasterxml.jackson.core/jackson-databind
    implementation("com.fasterxml.jackson.core:jackson-databind:2.22.1")
    // Source: https://mvnrepository.com/artifact/org.seleniumhq.selenium/selenium-java
    implementation("org.seleniumhq.selenium:selenium-java:4.40.0")
    // Source: https://mvnrepository.com/artifact/com.codeborne/selenide
    implementation("com.codeborne:selenide:7.16.2")
    // Source: https://mvnrepository.com/artifact/org.aeonbits.owner/owner
    implementation("org.aeonbits.owner:owner:1.0.12")
    // Source: https://mvnrepository.com/artifact/org.projectlombok/lombok
    implementation("org.projectlombok:lombok:1.18.46")
    // Source: https://mvnrepository.com/artifact/io.qameta.allure/allure-rest-assured
    implementation("io.qameta.allure:allure-rest-assured:2.29.0")
    // Source: https://mvnrepository.com/artifact/io.qameta.allure/allure-selenide
    implementation("io.qameta.allure:allure-selenide:2.29.0")

}

/// Конфигурация встроенной задачи test – запуск всех тестов
tasks.test {
    useJUnitPlatform {
        if (project.hasProperty("includeTags")) {
            includeTags(project.property("includeTags") as String)
        }
    }
    outputs.upToDateWhen { false }
    testLogging {
        events("passed", "skipped", "failed")
        showStandardStreams = true
    }
    systemProperty(
        "allure.results.directory",
        layout.buildDirectory.dir("allure-results").get().asFile.absolutePath
    )
    finalizedBy("allureReport")
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


// Задача 3: только для API-тестов
tasks.register<Test>("apiTest") {
    group = "test"
    description = "Runs API tests only"
    useJUnitPlatform { includeTags("api") }
    outputs.upToDateWhen { false }
    testLogging {
        events("passed", "skipped", "failed")
        showStandardStreams = true
    }
    systemProperty(
        "allure.results.directory",
        layout.buildDirectory.dir("allure-results").get().asFile.absolutePath
    )
    finalizedBy("allureReport")
}



// Задача 4: только для UI-тестов
tasks.register<Test>("UITest") {
    group = "test"
    description = "Runs UI tests only"
    useJUnitPlatform { includeTags("UI") }
    outputs.upToDateWhen { false }
    testLogging {
        events("passed", "skipped", "failed")
        showStandardStreams = true
    }
    systemProperty(
        "allure.results.directory",
        layout.buildDirectory.dir("allure-results").get().asFile.absolutePath
    )
    finalizedBy("allureReport")
}


// ==================== Очистка папки отчёта перед генерацией ====================
tasks.matching { it.name == "allureReport" }.configureEach {
    doFirst {
        val reportDir = layout.buildDirectory.dir("reports/allure-report").get().asFile
        if (reportDir.exists()) {
            reportDir.deleteRecursively()
        }
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