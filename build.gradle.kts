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


//Первая задача по запуску всех тестов Проекта
tasks.register<Test>("smokeTest") {
    group = "test";
    useJUnitPlatform(){
       includeTags("smoke")
    }
    println("========================\n" +
            "Test method start")
}

//Вторая задача по нотификациям

tasks.register<Test>(name = "finishedTest"){
    group = "test";
    useJUnitPlatform(){
        includeTags("smoke")
    }
    }

tasks.named ("finishedTest") {
    dependsOn("smokeTest")
    doLast {
        println("Test run is over")
        println("Test method end\n" +
                "========================")
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