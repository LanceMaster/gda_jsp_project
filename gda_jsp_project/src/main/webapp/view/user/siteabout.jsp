<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html lang="ko">
<head>
    <meta charset="UTF-8">
    <title>회사소개 | 코딩스쿨</title>
    <style>
        body { font-family: 'Noto Sans KR', Arial, sans-serif; background: #f7f9fb; margin: 0; color: #222; }
        .about-wrap { max-width: 820px; margin: 60px auto 40px; background: #fff; border-radius: 14px; box-shadow: 0 8px 24px rgba(52,100,219,0.08); padding: 48px 32px; }
        h1 { font-size: 2.8rem; margin-bottom: 10px; color: #3568ff; font-weight: 900;}
        .about-title { color: #00b894; margin-bottom: 6px; font-size: 1.2rem; font-weight: 600; letter-spacing: 2px;}
        .slogan { font-size: 1.5rem; margin-bottom: 36px; color: #222; font-weight: 700;}
        .section { margin-bottom: 48px;}
        .section-title { font-size: 1.3rem; color: #3568ff; font-weight: 700; margin-bottom: 12px;}
        .section-subtitle { font-size: 1.1rem; color: #444; margin-bottom: 14px;}
        .badge { background: #eaf3ff; color: #3568ff; padding: 3px 13px; border-radius: 15px; font-size: 0.96rem; margin-right: 8px;}
        ul { margin: 0 0 0 18px; padding: 0; }
        li { margin-bottom: 8px; font-size: 1rem;}
        .info-table { width: 100%; max-width: 410px; border-collapse: collapse; background: #f8fafd; margin: 12px 0 0 0;}
        .info-table td { padding: 8px 12px; border-bottom: 1px solid #e6eaf1; font-size: 0.99rem;}
        .info-table tr:last-child td { border-bottom: none;}
        .values-list { display: flex; gap: 28px; margin: 20px 0 20px 0; }
        .value-card { background: #eff4ff; padding: 18px 16px; border-radius: 10px; min-width: 160px; text-align: center; box-shadow: 0 2px 7px 0 #eef3fb;}
        .value-title { font-weight: 700; color: #3568ff; margin-bottom: 4px; font-size: 1.09rem;}
        .value-desc { font-size: 0.98rem; color: #444;}
        .cta { margin: 40px 0 0 0; text-align: center;}
        .cta-btn { background: #3568ff; color: #fff; font-size: 1.08rem; padding: 14px 34px; border-radius: 25px; border: none; font-weight: 700; box-shadow: 0 4px 18px 0 #dbe4fa; cursor: pointer; transition: background 0.18s;}
        .cta-btn:hover { background: #2849b7; text-decoration: none;}
        @media (max-width: 600px) {
            .about-wrap { padding: 16px 6px;}
            .values-list { flex-direction: column; gap: 10px;}
        }
    </style>
</head>
<body>
    <div class="about-wrap">
        <div class="about-title">About Coding School</div>
        <h1>코딩스쿨</h1>
        <div class="slogan">당신의 성장, <span style="color:#00b894;">진짜 실력</span>으로 완성됩니다.</div>

        <div class="section">
            <div class="section-title">🌟 우리는 이렇게 만듭니다</div>
            <div class="section-subtitle">
                단순히 듣는 강의가 아니라, <b>실습과 피드백, 그리고 진짜 커뮤니티</b>로<br>
                <span style="color:#3568ff; font-weight:700;">‘혼자서는 불가능한 성장’을 함께 경험하는 곳</span>이 바로 코딩스쿨입니다.<br>
                배움의 한계를 뛰어넘는, <b>실전형 인터넷 강의 플랫폼</b>을 지향합니다.
            </div>
        </div>

        <div class="section">
            <div class="section-title">🚀 코딩스쿨의 미션</div>
            <ul>
                <li><span class="badge">01</span>현실에 바로 적용 가능한 실무 중심 강의</li>
                <li><span class="badge">02</span>모든 수강생이 ‘코드 실습’을 직접 경험</li>
                <li><span class="badge">03</span>실시간 질문·답변, 피드백, 프로젝트 공유 커뮤니티</li>
                <li><span class="badge">04</span>자신만의 ‘성장 트랙’을 만들어가는 맞춤형 러닝</li>
            </ul>
        </div>

        <div class="section">
            <div class="section-title">🏆 코딩스쿨만의 차별점</div>
            <ul>
                <li><span class="badge">#실전</span>코딩실습, 과제, 미니프로젝트까지 ‘실무 역량’에 집중</li>
                <li><span class="badge">#소통</span>강사와 1:1 코드 리뷰·피드백 지원</li>
                <li><span class="badge">#트렌드</span>최신 기술, 라이브 특강</li>
                <li><span class="badge">#오픈</span>누구나 강사, 누구나 수강생! 열려있는 공유의 장</li>
            </ul>
        </div>

        <div class="section">
            <div class="section-title">🌱 우리가 믿는 가치</div>
            <div class="values-list">
                <div class="value-card">
                    <div class="value-title">성장</div>
                    <div class="value-desc">당신의 성장을<br>직접 증명할 수 있는 경험</div>
                </div>
                <div class="value-card">
                    <div class="value-title">실습과 도전</div>
                    <div class="value-desc">이론+실습+피드백<br>실무까지 연결되는 교육</div>
                </div>
                <div class="value-card">
                    <div class="value-title">연결</div>
                    <div class="value-desc">배우는 사람, 가르치는 사람, 함께하는 커뮤니티</div>
                </div>
                <div class="value-card">
                    <div class="value-title">공유와 소통</div>
                    <div class="value-desc">경험을 나누고 함께 만들어가는 성장 플랫폼</div>
                </div>
            </div>
        </div>

        <div class="cta">
            <p style="font-size:1.18rem; color:#222; font-weight:700; margin-bottom:10px;">
                새로운 도전을 함께 할 준비가 되었나요?<br>
                <span style="color:#3568ff;">코딩스쿨과 함께 ‘실력 있는 성장’을 시작하세요!</span>
            </p><br>
               <!-- 기존 button을 a 태그로 변경 -->
    <a href="${pageContext.request.contextPath}/user/signupform" class="cta-btn">
        코딩스쿨 가입하기
    </a>
        </div>
    </div>
</body>
</html>
