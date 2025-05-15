package model.provider;

import java.util.Map;

public class LectureSqlProvider {

    public String buildFilteredSortedQuery(Map<String, Object> params) {
        StringBuilder sql = new StringBuilder();

        sql.append("""
            SELECT 
              l.lecture_id AS lectureId,
              l.title,
              l.description,
              l.thumbnail,
              l.category,
              l.price,
              l.avg_rating AS avgRating,
              l.published_at AS publishedAt,
              (
                SELECT COUNT(*) 
                FROM user_interactions ui
                WHERE ui.target_type = 'LECTURE'
                  AND ui.target_id = l.lecture_id
                  AND ui.interaction_kind = 'FEEDBACK'
              ) AS reviewCount
            FROM lectures l
            WHERE l.status = 'PUBLISHED'
        """);

        // ✅ WHERE 조건: category 필터
        Object category = params.get("category");
        if (category != null && !category.toString().isBlank()) {
            sql.append(" AND l.category = #{category} ");
        }

        // ✅ 정렬 조건: sort 값에 따라 ORDER BY 다르게
        String sort = (String) params.get("sort");
        if ("popular".equals(sort)) {
            sql.append(" ORDER BY l.avg_rating DESC, reviewCount DESC ");
        } else {
            sql.append(" ORDER BY l.published_at DESC ");
        }

        return sql.toString();
    }
}
