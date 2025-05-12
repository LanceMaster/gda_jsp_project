<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
</head>
<body>
<p>
OnlineLecturePlatform/
├── 📁 src/
│   ├── 📁 controller/
│   │   ├── AuthController.java
│   │   ├── UserController.java
│   │   ├── LectureController.java
│   │   ├── PaymentController.java
│   │   ├── MentorController.java
│   │   ├── InquiryController.java
│   │   ├── AdminController.java
│   │   └── ProjectController.java
│   │
│   ├── 📁 dao/
│   │   ├── UserDAO.java
│   │   ├── LectureDAO.java
│   │   ├── EnrollmentDAO.java
│   │   ├── PaymentDAO.java
│   │   ├── MentorDAO.java
│   │   ├── InquiryDAO.java
│   │   ├── AdminDAO.java
│   │   └── ProjectDAO.java
│   │
│   ├── 📁 dto/
│   │   ├── UserDTO.java
│   │   ├── LectureDTO.java
│   │   ├── EnrollmentDTO.java
│   │   ├── PaymentDTO.java
│   │   ├── MentorDTO.java
│   │   ├── InquiryDTO.java
│   │   ├── AdminDTO.java
│   │   └── ProjectDTO.java
│   │
│   ├── 📁 service/
│   │   ├── AuthService.java
│   │   ├── UserService.java
│   │   ├── LectureService.java
│   │   ├── PaymentService.java
│   │   ├── MentorService.java
│   │   ├── InquiryService.java
│   │   └── ProjectService.java
│   │
│   ├── 📁 util/
│   │   ├── DBConnection.java
│   │   ├── EncryptUtil.java
│   │   ├── DateUtil.java
│   │   └── FileUploadUtil.java
│   │
│   └── 📁 config/
│       └── DBConfig.properties
│
├── 📁 WebContent/
│   ├── 📁 view/
│   │   ├── 📁 common/
│   │   │   ├── header.jsp
│   │   │   ├── footer.jsp
│   │   │   ├── layout.jsp
│   │   │   └── error.jsp
│   │   │
│   │   ├── 📁 auth/
│   │   │   ├── login.jsp
│   │   │   ├── register.jsp
│   │   │   ├── find_id.jsp
│   │   │   └── reset_password.jsp
│   │   │
│   │   ├── 📁 user/
│   │   │   ├── profile.jsp
│   │   │   └── mypage.jsp
│   │   │
│   │   ├── 📁 lecture/
│   │   │   ├── lecture_list.jsp
│   │   │   ├── lecture_detail.jsp
│   │   │   ├── lecture_create.jsp
│   │   │   ├── video_upload.jsp
│   │   │   └── cart.jsp
│   │   │
│   │   ├── 📁 payment/
│   │   │   ├── checkout.jsp
│   │   │   ├── payment_result.jsp
│   │   │   └── transfer_info.jsp
│   │   │
│   │   ├── 📁 mentor/
│   │   │   ├── mentor_apply.jsp
│   │   │   ├── mentoring_request.jsp
│   │   │   └── mentoring_detail.jsp
│   │   │
│   │   ├── 📁 inquiry/
│   │   │   ├── inquiry_list.jsp
│   │   │   ├── inquiry_write.jsp
│   │   │   └── inquiry_detail.jsp
│   │   │
│   │   ├── 📁 admin/
│   │   │   ├── dashboard.jsp
│   │   │   ├── user_list.jsp
│   │   │   ├── lecture_manage.jsp
│   │   │   └── payment_manage.jsp
│   │   │
│   │   └── 📁 project/
│   │       ├── project_list.jsp
│   │       ├── project_detail.jsp
│   │       └── comment_list.jsp
│
│   ├── 📁 css/
│   │   └── style.css
│
│   ├── 📁 js/
│   │   ├── common.js
│   │   └── validation.js
│
│   ├── 📁 image/
│   │   └── logo.png
│
│   └── 📄 index.jsp
│
├── 📄 web.xml
├── 📄 .gitignore
└── 📄 README.md
</p>
</body>
</html>