package model.provider;

import model.dto.LectureSearchCondition;
import org.apache.ibatis.jdbc.SQL;

public class LectureSqlProvider {

    // ✅ 강의 목록 조회 쿼리 (평점 + 리뷰수 기반 인기순 포함)
    public String findLectures(LectureSearchCondition cond) {
        return new SQL() {{
            SELECT("l.lecture_id AS lectureId");
            SELECT("l.title");
            SELECT("l.thumbnail");
            SELECT("l.category");
            SELECT("l.price");
            SELECT("ROUND(AVG(ui.rating), 1) AS avgRating");
            SELECT("COUNT(ui.interaction_id) AS reviewCount");
            SELECT("(ROUND(AVG(ui.rating), 1) * 0.9 + COUNT(ui.interaction_id) * 0.1) AS popularityScore");

            FROM("lectures l");
            LEFT_OUTER_JOIN("user_interactions ui ON l.lecture_id = ui.target_id " +
                            "AND ui.target_type = 'LECTURE' AND ui.interaction_kind = 'FEEDBACK'");
            WHERE("l.status = 'PUBLISHED'");

            if (cond.getCategory() != null && !cond.getCategory().isBlank()) {
                WHERE("l.category = #{category}");
            }

            if (cond.getKeyword() != null && !cond.getKeyword().isBlank()) {
                WHERE("(l.title LIKE CONCAT('%', #{keyword}, '%') " +
                      "OR l.description LIKE CONCAT('%', #{keyword}, '%'))");
            }

            GROUP_BY("l.lecture_id");

            if ("popular".equals(cond.getSort())) {
                ORDER_BY("popularityScore DESC");
            } else {
                ORDER_BY("l.published_at DESC");
            }
        }}.toString() + " LIMIT #{offset}, #{size}";
    }

    // ✅ 강의 개수 카운트 (페이징용)
    public static String countLectures(LectureSearchCondition cond) {
        return new SQL() {{
            SELECT("COUNT(DISTINCT l.lecture_id)");
            FROM("lectures l");
            LEFT_OUTER_JOIN("user_interactions ui ON l.lecture_id = ui.target_id " +
                            "AND ui.target_type = 'LECTURE' AND ui.interaction_kind = 'FEEDBACK'");
            WHERE("l.status = 'PUBLISHED'");

            if (cond.getCategory() != null && !cond.getCategory().isBlank()) {
                WHERE("l.category = #{category}");
            }

            if (cond.getKeyword() != null && !cond.getKeyword().isBlank()) {
                WHERE("(l.title LIKE CONCAT('%', #{keyword}, '%') " +
                      "OR l.description LIKE CONCAT('%', #{keyword}, '%'))");
            }

        }}.toString();
    }
    
    
}
