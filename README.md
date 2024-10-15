

# Hệ thống Quản lý Điểm danh bằng Thẻ từ với Spring Boot, React và AWS

## Mục tiêu của Dự án

Hệ thống này được xây dựng để quản lý việc điểm danh của người dùng bằng cách sử dụng thẻ từ. Backend sử dụng Spring Boot, frontend dùng React, và AWS để triển khai và lưu trữ dữ liệu. Trang admin cung cấp giao diện thân thiện giúp quản trị viên dễ dàng quản lý thông tin người dùng và lịch sử điểm danh.

## Tính năng

- **Điểm danh tự động**: ESP32 và RFID ghi nhận thông tin người dùng và thời gian điểm danh.
- **Trang admin quản lý**: Giao diện React để theo dõi và quản lý người dùng cũng như lịch sử điểm danh.
- **Tích hợp AWS**: Sử dụng AWS RDS để lưu trữ dữ liệu và AWS EC2 để triển khai ứng dụng.
- **Quản lý người dùng**: Thêm, sửa, xóa, và cập nhật thông tin người dùng.
- **Xem lịch sử điểm danh**: Lưu trữ và xem lịch sử điểm danh theo thời gian thực.

## Cấu trúc Dự án

1. **Backend - Spring Boot**: REST API xử lý dữ liệu và lưu trữ trong AWS RDS.
2. **Frontend - React**: Giao diện quản trị, tương tác với backend qua API.
3. **AWS**: 
   - **EC2**: Triển khai backend.
   - **RDS**: Lưu trữ cơ sở dữ liệu điểm danh.
   - **S3** *(tùy chọn)*: Lưu trữ tệp tĩnh hoặc ảnh.

## Yêu cầu Hệ thống

- **Spring Boot**: Java 11 hoặc 17
- **React**: Node.js và npm
- **AWS Account**: EC2, RDS
- **MySQL**: Trên RDS (hoặc PostgreSQL)

## Hướng dẫn Cài đặt

### 1. Cài đặt Backend (Spring Boot)

- Clone repository và chuyển vào thư mục backend:
  ```bash
  git clone <repository-url>
  cd attendance-backend
  ```

- Cấu hình `application.yaml` để kết nối với AWS RDS:
  ```yaml
    url: jdbc:mysql://iot.cdo2go0s62es.ap-southeast-2.rds.amazonaws.com:3306/iot
    username: admin
    password: 12345678
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

### 3. Triển khai trên AWS

- **EC2**: Đăng nhập vào AWS Console, tạo EC2 instance để chạy backend và deploy ứng dụng Spring Boot.
- **RDS**: Cấu hình MySQL instance trên AWS RDS để lưu trữ dữ liệu.
- **(Tùy chọn) S3**: Sử dụng để lưu trữ các tệp tĩnh nếu cần.

## Sử dụng

1. **Người dùng** quét thẻ RFID để ghi nhận điểm danh, dữ liệu sẽ được ESP32 gửi tới backend.
2. **Quản trị viên** truy cập giao diện admin để quản lý người dùng và xem lịch sử điểm danh.

--- 
