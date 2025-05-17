package model.provider;

import model.dto.LectureSearchCondition;
import org.apache.ibatis.jdbc.SQL;

public class LectureSqlProvider {

	 public String findLectures(LectureSearchCondition cond) {
	        return new SQL() {{
	            SELECT("l.lecture_id AS lectureId, l.title, l.thumbnail, l.category, l.price, l.avg_rating AS avgRating");
	            SELECT("(SELECT COUNT(*) FROM user_interactions WHERE target_type = 'LECTURE' AND target_id = l.lecture_id) AS reviewCount");
	            FROM("lectures l");
	            WHERE("l.status = 'PUBLISHED'");

	            if (cond.getCategory() != null && !cond.getCategory().isBlank()) {
	                WHERE("l.category = #{category}");
	            }

	            if (cond.getKeyword() != null && !cond.getKeyword().isBlank()) {
	                WHERE("(l.title LIKE CONCAT('%', #{keyword}, '%') OR l.description LIKE CONCAT('%', #{keyword}, '%'))");
	            }

	            if ("popular".equals(cond.getSort())) {
	                ORDER_BY("l.avg_rating DESC");
	            } else {
	                ORDER_BY("l.published_at DESC");
	            }
	        }}.toString() + " LIMIT #{offset}, #{size}";
	    }

    private static String getOrderByClause(LectureSearchCondition cond) {
        if ("popular".equals(cond.getSort())) {
            return " ORDER BY l.avg_rating DESC ";
        } else {
            return " ORDER BY l.published_at DESC ";
        }
    }
    
    public static String countLectures(LectureSearchCondition cond) {
        return new SQL() {{
            SELECT("COUNT(*)");
            FROM("lectures l");
            WHERE("l.status = 'PUBLISHED'");

            if (cond.getCategory() != null && !cond.getCategory().isEmpty()) {
                WHERE("l.category = #{category}");
            }

            if (cond.getKeyword() != null && !cond.getKeyword().isEmpty()) {
                WHERE("(l.title LIKE CONCAT('%', #{keyword}, '%') OR l.description LIKE CONCAT('%', #{keyword}, '%'))");
            }

        }}.toString();
    }
}
