FROM eclipse-temurin:21-jdk

WORKDIR /app

COPY . .

RUN javac *.java

EXPOSE 10000

CMD ["java", "WebServer"]