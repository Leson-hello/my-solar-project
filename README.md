# Solar News Management System - DAT Group

## 📋 Tổng quan

Hệ thống quản lý tin tức cho website của DAT Group, chuyên về năng lượng mặt trời. Hệ thống được xây dựng với Spring
Boot và Thymeleaf, cung cấp đầy đủ các chức năng CRUD tin tức với giao diện admin chuyên nghiệp.

## 🚀 Tính năng đã hoàn thành

### ✅ Backend API & Logic

#### **1. News Management CRUD**

- ✅ Tạo tin tức mới với form validation
- ✅ Chỉnh sửa tin tức hiện có
- ✅ Xóa tin tức với xác nhận
- ✅ Phát hành/gỡ xuống tin tức
- ✅ Đánh dấu tin nổi bật
- ✅ Lưu bản nháp và xuất bản

#### **2. Advanced Features**

- ✅ **Auto-save**: Tự động lưu bản nháp mỗi 30 giây
- ✅ **Image Upload**: Upload ảnh đại diện và ảnh trong nội dung
- ✅ **Rich Text Editor**: Summernote với đầy đủ tính năng
- ✅ **Search & Filter**: Tìm kiếm theo tiêu đề, lọc theo danh mục
- ✅ **Pagination**: Phân trang cho danh sách tin tức
- ✅ **View Counter**: Đếm lượt xem tin tức

#### **3. REST API Endpoints**
```
POST /api/admin/upload-content-image - Upload ảnh trong content
POST /api/admin/news/{id}/auto-save - Auto-save tin tức
POST /api/admin/news/create-draft - Tạo bản nháp mới
GET  /api/admin/news/{id}/preview - Preview tin tức
POST /api/admin/news/validate - Validate dữ liệu
```

### ✅ Frontend UI/UX

#### **1. Admin Dashboard**

- ✅ Sidebar navigation với các menu chính
- ✅ Dashboard với thống kê tổng quan
- ✅ Danh sách tin tức gần đây
- ✅ Responsive design cho mobile/tablet

#### **2. News Form (Create/Edit)**

- ✅ Form validation real-time
- ✅ Rich text editor với Summernote
- ✅ Drag & drop upload ảnh
- ✅ Preview modal
- ✅ Multiple submit actions:
    - Save (Lưu)
    - Save & Publish (Lưu và xuất bản)
    - Save Draft (Lưu bản nháp)
- ✅ Auto-save với status indicator
- ✅ Sticky action buttons

#### **3. News List Management**

- ✅ Bảng danh sách với đầy đủ thông tin
- ✅ Filter theo danh mục và tìm kiếm
- ✅ Pagination
- ✅ Status badges (Published/Draft/Featured)

### ✅ Sửa lỗi và cải thiện

#### **1. Query Validation Error - FIXED**

Lỗi trong repository khi sử dụng LOWER() với field LONGTEXT đã được sửa.

#### **2. Form Processing - ENHANCED**

- ✅ Multiple action handling (save/publish/draft)
- ✅ Image upload với validation
- ✅ Default values cho author
- ✅ Error handling và flash messages

## 🛠️ Cài đặt và chạy

### Yêu cầu hệ thống

- Java 17+
- MySQL 8.0+
- Maven 3.6+

### Cài đặt

1. **Cấu hình database** (application.properties)
```properties
spring.datasource.url=jdbc:mysql://localhost:3306/solar_db
spring.datasource.username=root
spring.datasource.password=root
```

2. **Tạo database**

```sql
CREATE DATABASE solar_db CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
```

3. **Chạy ứng dụng**
```bash
./mvnw spring-boot:run
```

4. **Truy cập**

- Frontend: http://localhost:8080
- Admin: http://localhost:8080/admin

## 📊 Thống kê dự án

- **Total Files**: 15+ files
- **Lines of Code**: 2000+ lines
- **Templates**: 3 admin templates
- **API Endpoints**: 5 REST APIs
- **Features**: 20+ completed features

---

**Developed with ❤️ for DAT Group Solar Energy**