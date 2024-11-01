

# Hệ thống Quản lý Điểm danh bằng Thẻ từ với Spring Boot, React và MySQL

## Mục tiêu của Dự án

Hệ thống này được xây dựng để quản lý việc điểm danh của người dùng bằng cách sử dụng thẻ từ. Backend sử dụng Spring Boot, frontend dùng React, và MySQLlưu trữ dữ liệu. Trang admin cung cấp giao diện thân thiện giúp quản trị viên dễ dàng quản lý thông tin người dùng và lịch sử điểm danh.

## Tính năng

- **Điểm danh tự động**: ESP32 và RFID ghi nhận thông tin người dùng và thời gian điểm danh.
- **Trang admin quản lý**: Giao diện React để theo dõi và quản lý người dùng cũng như lịch sử điểm danh.
- **MySQL**: Sử dụng MySQL để lưu trữ dữ liệu 
- **Quản lý người dùng**: Thêm, sửa, xóa, và cập nhật thông tin người dùng.
- **Xem lịch sử điểm danh**: Lưu trữ và xem lịch sử điểm danh theo thời gian thực.

## Cấu trúc Dự án

1. **Backend - Spring Boot**: REST API xử lý dữ liệu và lưu trữ trong MySQL.
2. **Frontend - React**: Giao diện quản trị, tương tác với backend qua API.
3. **MySQL**: Lưu trữ cơ sở dữ liệu điểm danh.

## Yêu cầu Hệ thống

- **Spring Boot**: Java 11 hoặc 17
- **React**: Node.js và npm
- **MySQL**: 8.0.33

## Hướng dẫn Cài đặt

### 1. Cài đặt Backend (Spring Boot)

- Clone repository và chuyển vào thư mục backend:
  ```bash
  git clone <repository-url>
  cd attendance-backend
  ```

- Cấu hình `application.yaml` để kết nối với MySQL:
  ```yaml
    url: jdbc:mysql://localhost:3306/iot
    username: 
    password: 
  ```

- Xây dựng và chạy ứng dụng Spring Boot:
  ```bash
  ./mvnw spring-boot:run
  ```

### 2. Cài đặt Frontend (React)

- Chuyển vào thư mục frontend và cài đặt các package:
  ```bash
  cd attendance-frontend
  npm install
  ```

- Cấu hình tệp `.env` để trỏ tới backend API:
  ```env
  REACT_APP_API_URL=http://<backend-ec2-instance-url>/api
  ```

- Chạy ứng dụng React:
  ```bash
  npm start
  ```

## Sử dụng

1. **Người dùng** quét thẻ RFID để ghi nhận điểm danh, dữ liệu sẽ được ESP32 gửi tới backend.
2. **Quản trị viên** truy cập giao diện admin để quản lý người dùng và xem lịch sử điểm danh.

--- 
