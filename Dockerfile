FROM eclipse-temurin:17-jdk-jammy as base

WORKDIR /app

COPY .mvn/ .mvn
COPY mvnw pom.xml ./
COPY src ./src
RUN chmod 777 mvnw && ./mvnw dependency:resolve

FROM base as development
COPY certificates/GCSystemPaymentHomo.p12 ./certificates/
CMD ["./mvnw", "spring-boot:run", "-Dspring-boot.run.profiles=development", "-Dspring-boot.run.jvmArguments='-agentlib:jdwp=transport=dt_socket,server=y,suspend=n,address=*:8000'"]