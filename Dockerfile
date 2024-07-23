FROM openjdk:22-jdk

ENV PROJECT_HOME /usr/src/courses
ENV JAR_NAME courses.jar

# Create destination directory
RUN mkdir -p $PROJECT_HOME
WORKDIR $PROJECT_HOME

# Bundle app source
COPY . .

# Package the application as a JAR file
RUN ./mvnw clean package -DskipTests

# Move file
RUN mv $PROJECT_HOME/target/$JAR_NAME $PROJECT_HOME/

ENTRYPOINT ["java", "-jar", "-Dspring.profiles.active=prod", "courses.jar"]