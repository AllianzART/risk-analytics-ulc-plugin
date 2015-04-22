grails.project.class.dir = "target/classes"
grails.project.test.class.dir = "target/test-classes"
grails.project.test.reports.dir = "target/test-reports"

grails.project.dependency.resolver = "maven"

grails.project.dependency.resolution = {
    // inherit Grails' default dependencies
    inherits("global") {
        // uncomment to disable ehcache
        // excludes 'ehcache'
    }
    log "warn" // log level of Ivy resolver, either 'error', 'warn', 'info', 'debug' or 'verbose'
    repositories {
        grailsHome()
        mavenLocal()
        mavenRepo(name: "zh-artisan-test", url: "http://zh-artisan-test.art-allianz.com:8085/nexus/content/groups/public/") {
            updatePolicy System.getProperty('snapshotUpdatePolicy') ?: 'daily'
        }

        grailsCentral()
        mavenCentral()
    }
    dependencies {
        // specify dependencies here under either 'build', 'compile', 'runtime', 'test' or 'provided' scopes eg.

        // runtime 'mysql:mysql-connector-java:5.1.21'
    }

    plugins {
        runtime ":release:3.0.1", {
            excludes "groovy"
        }
    }
}

grails.project.repos.default = "pillarone"

grails.project.dependency.distribution = {
    String password = ""
    String user = ""
    String scpUrl
    try {
        Properties properties = new Properties()
        properties.load(new File("${userHome}/deployInfo.properties").newInputStream())
        user = properties.get("user")
        password = properties.get("password")
        scpUrl = properties.get("url")
        remoteRepository(id: "pillarone", url: scpUrl) {
            authentication username: user, password: password
        }
    } catch (Throwable t) {
        println "deployInfo.properties not found. $t.message"
    }
}
