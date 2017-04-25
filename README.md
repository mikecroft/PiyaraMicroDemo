# Clustered CDI Event Bus Demo - Extended with Kotlin

To get this to work properly with Kotlin, I changed the `Event<T>` type to be `String`, rather than `Stock`, since I'd rewritten the `Stock` class in Kotlin, meaning the StockSessionManager.java couldn't deserialise it properly. Changing it to just send strings made sense. I could have made it better by using JSON-B, or just serialising the Stock object properly but, having never used Kotlin before, it was a bit of a stretch for one evening.

### Building

1. Go to the `KotlinTicker` directory and run `gradle build`
2. From the root directory, run `mvn clean install`

### Running

1. Launch the KotlinTicker first:
`java -jar payara-micro.jar --deploy KotlinTicker/build/libs/KotlinTicker.war`

2. Once started, launch the StockWeb application:
`java -jar payara-micro.jar --deploy StockWeb/target/StockWeb-1.0-SNAPSHOT.war --autobindhttp`

The ticker should be on port 8080, so the web app should be on 8081, so visit this URL to see stock updates:

http://localhost:8081/StockWeb-1.0-SNAPSHOT/
