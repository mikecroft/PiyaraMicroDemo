# Requirements
JDK 8
Maven 3
Multicast enabled
Payara Micro

# Instructions
To compile, run: `mvn clean install` from the root directory.

Once it has compiled, run it by:
* Start the StockTicker project on port 9999: java -jar ${microLocation} --deploy StockTicker/target/StockTicker-1.0-SNAPSHOT.war --port 9999
* Start the StockWeb project: java -jar ${microLocation} --deploy StockWeb/target/StockWeb-1.0-SNAPSHOT.war --autoBindHttp
* Start the StockMicro project: java -jar ${microLocation} --deploy StockMicroprofile/target/StockMicroprofile.war --autoBindHttp

The StockWeb app should be available at: http://localhost:8080/StockWeb-1.0-SNAPSHOT/index.jsp
The StockMicro REST endpoint should be available at http://localhost:8081/StockMicroprofile/webresources/rest
