<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>


<!DOCTYPE html>
<html lang="ko">
<head>
  <meta charset="UTF-8">
  <title>내 강의 관리</title>
  <meta name="viewport" content="width=device-width, initial-scale=1">
  <link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.5.2/css/bootstrap.min.css">
  <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.2/css/all.min.css">
  <style>
    body { background: #f9fafb; font-family: 'Apple SD Gothic Neo', 'Segoe UI', sans-serif; }
    .container { max-width: 1100px; margin: 40px auto; }
    .lecture-table th, .lecture-table td { vertical-align: middle; }
    .lecture-thumb { width: 70px; height: 48px; object-fit: cover; border-radius: 7px; }
    .badge-status { padding: 5px 12px; border-radius: 10px; font-size: 0.96rem; }
    .badge-published { background: #d1fae5; color: #065f46; }
    .badge-draft { background: #fef3c7; color: #b45309; }
    .switch { position: relative; display: inline-block; width: 38px; height: 22px; }
    .switch input { opacity: 0; width: 0; height: 0; }
    .slider { position: absolute; cursor: pointer; top: 0; left: 0; right: 0; bottom: 0;
      background-color: #ccc; transition: .4s; border-radius: 16px;}
    .slider:before { position: absolute; content: ""; height: 16px; width: 16px; left: 3px; bottom: 3px;
      background-color: white; transition: .4s; border-radius: 50%; }
    input:checked + .slider { background-color: #6c6ce5; }
    input:checked + .slider:before { transform: translateX(16px); }
    .table-responsive { border-radius: 18px; box-shadow: 0 3px 16px #eee; }
    .edit-field { width: 95%; border: 1px solid #ddd; border-radius: 6px; padding: 2px 6px; }
    @media (max-width: 768px) {
      .container { padding: 0 6px; }
      .lecture-table td, .lecture-table th { font-size: 0.94rem; }
    }
  </style>
</head>
<body>
<div class="container">
  <h2 class="mb-4 font-weight-bold"><i class="fa-solid fa-graduation-cap"></i> 내 강의 관리</h2>

  <!-- ✅ 필터 폼 -->
  <form id="lectureFilterForm" class="form-inline mb-3 justify-content-between" method="get" action="${pageContext.request.contextPath}/lecture/management">
    <div>
      <input name="search" class="form-control mr-2" type="search" style="width:180px;"
             value="${fn:escapeXml(param.search)}" placeholder="강의명/설명 검색">
      <select name="category" class="form-control mr-2">
        <option value="">카테고리 전체</option>
        <c:forEach var="cat" items="${categories}">
          <option value="${cat}" <c:if test="${param.category == cat}">selected</c:if>>${cat}</option>
        </c:forEach>
      </select>
      <select name="status" class="form-control mr-2">
        <option value="">상태 전체</option>
        <option value="PUBLISHED" <c:if test="${param.status == 'PUBLISHED'}">selected</c:if>>공개중</option>
        <option value="DRAFT" <c:if test="${param.status == 'DRAFT'}">selected</c:if>>임시저장</option>
      </select>
      <button type="submit" class="btn btn-sm btn-outline-primary">검색</button>
    </div>
    <div>
      <select name="sort" class="form-control">
        <option value="created_at" <c:if test="${param.sort == 'created_at'}">selected</c:if>>최신순</option>
        <option value="price_desc" <c:if test="${param.sort == 'price_desc'}">selected</c:if>>가격높은순</option>
        <option value="price_asc" <c:if test="${param.sort == 'price_asc'}">selected</c:if>>가격낮은순</option>
        <option value="rating_desc" <c:if test="${param.sort == 'rating_desc'}">selected</c:if>>평점높은순</option>
      </select>
    </div>
  </form>

  <!-- ✅ 강의 리스트 -->
  <div class="table-responsive mb-5">
    <table class="table table-hover lecture-table">
      <thead class="bg-light">
        <tr>
          <th>썸네일</th>
          <th>강의명</th>
          <th>설명</th>
          <th>카테고리</th>
          <th>가격(₩)</th>
          <th>평점</th>
          <th>상태</th>
          <th>공개</th>
          <th>관리</th>
        </tr>
      </thead>
      <tbody>
        <c:forEach var="lecture" items="${myLectures}">
          <tr id="row-${lecture.lectureId}">
            <img src="${pageContext.request.contextPath}${fn:escapeXml(lecture.thumbnail)}" class="lecture-thumb" alt="썸네일">
            </td>
            <td>
              <span class="editable" data-field="title" data-id="${lecture.lectureId}">
                <span class="edit-span">${lecture.title}</span>
                <input class="edit-field d-none" name="title" value="${lecture.title}">
              </span>
            </td>
            <td>
              <span class="editable" data-field="description" data-id="${lecture.lectureId}">
                <span class="edit-span">${lecture.description}</span>
                <input class="edit-field d-none" value="${lecture.description}">
              </span>
            </td>
            <td>
              <span class="editable" data-field="category" data-id="${lecture.lectureId}">
                <span class="edit-span">${lecture.category}</span>
                <select class="edit-field d-none">
                  <c:forEach var="cat" items="${categories}">
                    <option value="${cat}" <c:if test="${lecture.category == cat}">selected</c:if>>${cat}</option>
                  </c:forEach>
                </select>
              </span>
            </td>
            <td>
              <span class="editable" data-field="price" data-id="${lecture.lectureId}">
                <span class="edit-span"><fmt:formatNumber value="${lecture.price}" type="number"/></span>
                <input type="number" class="edit-field d-none" value="${lecture.price}" min="0">
              </span>
            </td>
            <td><fmt:formatNumber value="${lecture.avgRating}" pattern="#.0"/> / 5</td>
            <td>
              <span class="badge badge-status ${lecture.status eq 'PUBLISHED' ? 'badge-published' : 'badge-draft'}">
                <c:out value="${lecture.status eq 'PUBLISHED' ? '공개중' : '임시저장'}"/>
              </span>
            </td>
            <td>
              <label class="switch">
                <input type="checkbox" onchange="toggleStatus(${lecture.lectureId}, this.checked)"
                  <c:if test="${lecture.status eq 'PUBLISHED'}">checked</c:if> >
                <span class="slider"></span>
              </label>
            </td>
            <td>
              <div class="btn-group btn-group-sm">
                <a class="btn btn-info" href="${pageContext.request.contextPath}/lecture/play?lectureId=${lecture.lectureId}">
                  <i class="fa fa-eye"></i> 상세
                </a>
                <button class="btn btn-danger" onclick="deleteLecture(${lecture.lectureId}, '${lecture.title}')">
                  <i class="fa fa-trash"></i> 삭제
                </button>
              </div>
            </td>
          </tr>
        </c:forEach>
      </tbody>
    </table>
  </div>

  <div id="toast" class="alert alert-success" style="position:fixed;bottom:22px;right:22px;display:none;z-index:10000;"></div>
</div>

<!-- 삭제 모달 -->
<div class="modal fade" id="deleteModal" tabindex="-1">
  <div class="modal-dialog">
    <div class="modal-content p-4">
      <div class="modal-header">
        <h5 class="modal-title">강의 삭제 확인</h5>
        <button type="button" class="close" data-dismiss="modal"><span>&times;</span></button>
      </div>
      <div class="modal-body">
        <p id="deleteLectureMsg"></p>
      </div>
      <div class="modal-footer">
        <button type="button" class="btn btn-danger" id="confirmDeleteBtn">삭제</button>
        <button type="button" class="btn btn-secondary" data-dismiss="modal">취소</button>
      </div>
    </div>
  </div>
</div>

<!-- Scripts -->
<script src="https://code.jquery.com/jquery-3.6.0.min.js"></script>
<script src="https://stackpath.bootstrapcdn.com/bootstrap/4.5.2/js/bootstrap.min.js"></script>
<script>
function showToast(msg, type = 'success') {
  $("#toast").removeClass("alert-success alert-danger").addClass("alert-" + type)
    .text(msg).fadeIn(200).delay(1200).fadeOut(400);
}

$(document).on('click', '.editable', function(e) {
  if ($(e.target).hasClass('edit-field')) return;
  $(this).find('.edit-span').addClass('d-none');
  $(this).find('.edit-field').removeClass('d-none').focus().select();
});

$(document).on('blur', '.edit-field', function() {
  let $td = $(this).closest('.editable');
  let val = $(this).val();
  let field = $td.data('field'), id = $td.data('id');
  $.ajax({
    url: '${pageContext.request.contextPath}/lecture/updateField',
    type: 'POST',
    data: { lectureId: id, field: field, value: val },
    success: function() {
      if ($td.find('.edit-field').is('select')) {
        $td.find('.edit-span').text($td.find('option:selected').text());
      } else {
        $td.find('.edit-span').text(val);
      }
      $td.find('.edit-field').addClass('d-none');
      $td.find('.edit-span').removeClass('d-none');
      showToast('저장 완료');
    },
    error: function() {
      showToast('저장 실패', 'danger');
    }
  });
});

$(document).on('keyup', '.edit-field', function(e) {
  if (e.key === 'Enter') $(this).blur();
});

function toggleStatus(id, isChecked) {
  $.post('${pageContext.request.contextPath}/lecture/toggleStatus',
    { lectureId: id, status: isChecked ? 'PUBLISHED' : 'DRAFT' },
    function() {
      showToast('상태 변경 완료');
      location.reload();
    }).fail(function() {
      showToast('상태 변경 실패', 'danger');
    });
}

let deleteId = null;
function deleteLecture(id, title) {
  deleteId = id;
  $('#deleteLectureMsg').html("[" + title + "] 강의를 정말 삭제하시겠습니까?<br>삭제 후 10초간 <b>되돌리기</b> 가능");
  $('#deleteModal').modal('show');
}

$('#confirmDeleteBtn').click(function() {
  $.ajax({
    url: '${pageContext.request.contextPath}/lecture/delete',
    type: 'POST',
    data: { lectureId: deleteId },
    success: function() {
      $('#row-' + deleteId).fadeOut();
      showToast('삭제 완료! 되돌리기(Undo)는 10초간 상단에 표시됩니다.');
      $('#deleteModal').modal('hide');
    },
    error: function() {
      showToast('삭제 실패', 'danger');
    }
  });
});
</script>
</body>
</html>
