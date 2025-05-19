package sql;

import java.util.Map;

public class LectureSqlBuilder {

    public static String buildSearchQuery(Map<String, Object> params) {
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT l.*, ");
        sql.append("  (SELECT COUNT(*) FROM user_interactions ");
        sql.append("   WHERE target_type='LECTURE' AND target_id = l.lecture_id) AS reviewCount, ");
        sql.append("  (SELECT ROUND(AVG(rating),1) FROM user_interactions ");
        sql.append("   WHERE target_type='LECTURE' AND target_id = l.lecture_id) AS avgRating ");
        sql.append("FROM lectures l ");
        sql.append("WHERE l.status = 'PUBLISHED' ");

        if (params.get("category") != null && !params.get("category").toString().isBlank()) {
            sql.append("AND l.category = #{category} ");
        }

        if (params.get("keyword") != null && !params.get("keyword").toString().isBlank()) {
            sql.append("AND (l.title LIKE CONCAT('%', #{keyword}, '%') ");
            sql.append("OR l.description LIKE CONCAT('%', #{keyword}, '%')) ");
        }

        if ("popular".equals(params.get("sort"))) {
            sql.append("ORDER BY avgRating DESC ");
        } else {
            sql.append("ORDER BY l.published_at DESC ");
        }

        sql.append("LIMIT #{size} OFFSET #{offset}");

        return sql.toString();
    }
}
