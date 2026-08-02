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
    // Source: https://mvnrepository.com/artifact/org.assertj/assertj-core
    testImplementation("org.assertj:assertj-core:3.27.7")
    // Source: https://mvnrepository.com/artifact/org.awaitility/awaitility
    testImplementation("org.awaitility:awaitility:4.3.0")
    // Source: https://mvnrepository.com/artifact/io.rest-assured/rest-assured
    implementation("io.rest-assured:rest-assured:5.5.6")
    // Source: https://mvnrepository.com/artifact/com.fasterxml.jackson.core/jackson-databind
    implementation("com.fasterxml.jackson.core:jackson-databind:2.22.1")

}

/// Конфигурация встроенной задачи test – запуск всех тестов
tasks.test {
    useJUnitPlatform {
        // Если передан параметр -PincludeTags=..., используем его
        if (project.hasProperty("includeTags")) {
            includeTags(project.property("includeTags") as String)
        }
    }
    outputs.upToDateWhen { false }   // всегда считать задачу устаревшей
    // Всегда показывать вывод тестов в консоли (чтобы видеть сообщения ассертов)
    testLogging {
        events("passed", "skipped", "failed")
        showStandardStreams = true
    }
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
    useJUnitPlatform {
        includeTags("api")
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