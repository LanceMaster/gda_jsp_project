<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>회원 목록</title>
    <style>
        .table-hover tbody tr:hover {
            background-color: #f5f5f5;
            cursor: pointer;
        }

        .pagination li a {
            color: #5A5AFF;
        }

        .pagination .active a {
            background-color: #5A5AFF;
            border-color: #5A5AFF;
            color: white;
        }
    </style>
</head>
<body>
<div class="container mt-4">
    <div class="d-flex mb-3">
        <a href="${pageContext.request.contextPath}/admin/userlist" class="btn btn-outline-primary mr-2">회원목록</a>
        <a href="${pageContext.request.contextPath}/admin/teacherlist" class="btn btn-outline-primary">강사목록</a>
    </div>

    <input type="text" class="form-control mb-3" placeholder="Search users...">

    <table class="table table-bordered table-hover text-center">
        <thead class="thead-light">
            <tr>
                <th>User ID</th>
                <th>회원이름</th>
                <th>이메일</th>
                <th>생년월일</th>
                <th>연락처</th>
            </tr>
        </thead>
        <tbody>
            <c:forEach var="user" items="${userList}">
                <tr class="clickable-row" data-userid="${user.userId}">
                    <td>${user.userId}</td>
                    <td>${user.name}</td>
                    <td>${user.email}</td>
                    <td>${user.birthdate}</td>
                    <td>${user.phone}</td>
                </tr>
            </c:forEach>
        </tbody>
    </table>

    <!-- Pagination -->
    <nav aria-label="Page navigation">
        <ul class="pagination justify-content-center">
            <c:if test="${pageInfo.hasPreviousPage}">
                <li class="page-item">
                    <a class="page-link" href="?pageNum=${pageInfo.prePage}">&lt; 이전 페이지</a>
                </li>
            </c:if>

            <c:forEach var="i" begin="${pageInfo.navigateFirstPage}" end="${pageInfo.navigateLastPage}">
                <li class="page-item ${i == pageInfo.pageNum ? 'active' : ''}">
                    <a class="page-link" href="?pageNum=${i}">${i}</a>
                </li>
            </c:forEach>

            <c:if test="${pageInfo.hasNextPage}">
                <li class="page-item">
                    <a class="page-link" href="?pageNum=${pageInfo.nextPage}">다음 페이지 &gt;</a>
                </li>
            </c:if>
        </ul>
    </nav>
</div>

<!-- Modal -->
<div class="modal fade" id="userDetailModal" tabindex="-1" role="dialog" aria-labelledby="userDetailModalLabel" aria-hidden="true">
    <div class="modal-dialog modal-dialog-centered" role="document">
        <div class="modal-content">
            <div class="modal-header">
                <h5 class="modal-title">회원 상세 정보</h5>
                <button type="button" class="close" data-dismiss="modal" aria-label="닫기">
                    <span aria-hidden="true">&times;</span>
                </button>
            </div>
            <div class="modal-body">
                <p><strong>User ID:</strong> <span id="modalUserId"></span></p>
                <p><strong>이름:</strong> <span id="modalName"></span></p>
                <p><strong>이메일:</strong> <span id="modalEmail"></span></p>
                <p><strong>생년월일:</strong> <span id="modalBirthdate"></span></p>
                <p><strong>연락처:</strong> <span id="modalPhone"></span></p>
            </div>
            <div class="modal-footer">
                <button type="button" class="btn btn-secondary" data-dismiss="modal">닫기</button>
            </div>
        </div>
    </div>
</div>

<script>
document.addEventListener("DOMContentLoaded", function() {
    const rows = document.querySelectorAll(".clickable-row");

    rows.forEach(row => {
        row.addEventListener("click", function() {
            const userId = this.dataset.userid;

            fetch(`/admin/userdetail?userid=${userId}`)
                .then(response => {
                    if (!response.ok) throw new Error("네트워크 오류");
                    return response.json();
                })
                .then(data => {
                    document.getElementById("modalUserId").innerText = data.userId;
                    document.getElementById("modalName").innerText = data.name;
                    document.getElementById("modalEmail").innerText = data.email;
                    document.getElementById("modalBirthdate").innerText = data.birthdate;
                    document.getElementById("modalPhone").innerText = data.phone;

                    $('#userDetailModal').modal('show');
                })
                .catch(error => {
                    alert("상세 정보를 불러오는 데 실패했습니다.");
                    console.error(error);
                });
        });
    });
});
</script>

</body>
</html>
