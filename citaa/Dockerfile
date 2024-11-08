# Sử dụng JDK để build
FROM openjdk:17-jdk-slim AS build

# Đặt thư mục làm việc
WORKDIR /app

# Copy toàn bộ project vào container
COPY . .

# Build ứng dụng
RUN ./mvnw clean package -DskipTests

# Tạo Docker image cho ứng dụng
FROM openjdk:17-jdk-slim
WORKDIR /app
COPY --from=build /app/target/*.jar app.jar

# Expose cổng 8080
EXPOSE 8080

# Khởi chạy ứng dụng
ENTRYPOINT ["java", "-jar", "app.jar"]
