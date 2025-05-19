package model.mapper;

import model.dto.ContentDTO;
import model.dto.LectureCardDTO;
import model.dto.LectureDTO;
import model.dto.LectureSearchCondition;
import model.provider.LectureSqlProvider;
import sql.LectureSqlBuilder;

import org.apache.ibatis.annotations.*;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Param;
import java.util.List;
import java.util.Map;

public interface LectureMapper {

	// ✅ 3. 전체 강의 목록 조회
	@Select("""
			    SELECT
			        l.lecture_id AS lectureId,
			        l.title,
			        l.description,
			        l.thumbnail,
			        l.category,
			        l.price,
			        l.avg_rating AS avgRating,
			        l.instructor_id AS instructorId,
			        (SELECT COUNT(*) FROM user_interactions
			         WHERE target_type = 'LECTURE'
			           AND target_id = l.lecture_id
			           AND interaction_kind = 'FEEDBACK') AS reviewCount
			    FROM lectures l
			    WHERE l.status = 'PUBLISHED'
			    ORDER BY l.created_at DESC
			""")
	List<LectureDTO> getAllLectures();

	// ✅ 4. 카테고리별 조회
	@Select("""
			    SELECT
			        lecture_id AS lectureId,
			        title,
			        description,
			        thumbnail,
			        category,
			        price,
			        avg_rating AS avgRating,
			        instructor_id AS instructorId,
			        published_at AS publishedAt,
			        created_at AS createdAt,
			        updated_at AS updatedAt
			    FROM lectures
			    WHERE category = #{category}
			    AND status = 'PUBLISHED'
			    ORDER BY created_at DESC
			""")
	List<LectureDTO> selectByCategory(@Param("category") String category);

	@Select("SELECT content_id, lecture_id, title, url, duration, sequence "
			+ "FROM contents WHERE lecture_id = #{lectureId} ORDER BY sequence ASC")
	List<ContentDTO> selectAllContents(@Param("lectureId") int lectureId);

	// ✅ 5. 키워드 검색 (제목 + 설명 LIKE)
	@Select("""
			    SELECT
			        lecture_id AS lectureId,
			        title,
			        description,
			        thumbnail,
			        category,
			        price,
			        avg_rating AS avgRating,
			        instructor_id AS instructorId,
			        published_at AS publishedAt,
			        created_at AS createdAt,
			        updated_at AS updatedAt
			    FROM lectures
			    WHERE (title LIKE CONCAT('%', #{keyword}, '%')
			        OR description LIKE CONCAT('%', #{keyword}, '%'))
			    AND status = 'PUBLISHED'
			    ORDER BY created_at DESC
			""")
	List<LectureDTO> searchByKeyword(@Param("keyword") String keyword);

	@Select("""
			    SELECT
			        lecture_id AS lectureId,
			        title,
			        description,
			        thumbnail,
			        category,
			        price,
			        avg_rating AS avgRating,
			        instructor_id AS instructorId,
			        published_at AS publishedAt,
			        created_at AS createdAt,
			        updated_at AS updatedAt
			    FROM lectures
			    WHERE lecture_id = #{lectureId}
			""")
	LectureDTO getLectureById(@Param("lectureId") int lectureId);

	// ✅ 7. 첫 번째 콘텐츠 조회 (order_no = 1)
	@Select("""
			    SELECT
			        content_id AS contentId,
			        lecture_id AS lectureId,
			        type,
			        title,
			        url,
			        duration,
			        order_no AS orderNo,
			        created_at AS createdAt
			    FROM lecture_contents
			    WHERE lecture_id = #{lectureId}
			    ORDER BY order_no ASC
			    LIMIT 1
			""")
	ContentDTO getFirstContentByLectureId(@Param("lectureId") int lectureId);

	@Select("""
			    SELECT title FROM lectures WHERE lecture_id = #{lectureId}
			""")
	String selectTitleById(@Param("lectureId") int lectureId);

	// ✅ 8. 강의 평균 평점 갱신
	@Update("""
			    UPDATE lectures
			    SET avg_rating = (
			        SELECT ROUND(AVG(rating), 1)
			        FROM user_interactions
			        WHERE target_type = 'LECTURE'
			        AND target_id = #{lectureId}
			        AND interaction_kind = 'FEEDBACK'
			    )
			    WHERE lecture_id = #{lectureId}
			""")
	void updateLectureRating(@Param("lectureId") int lectureId);

	// ✅ 9. 동적 정렬 + 카테고리 필터 (XML에 정의됨)
	List<LectureDTO> getLecturesFilteredSorted(Map<String, Object> params);

	@Select("""
			    <script>
			        SELECT * FROM lecture
			        WHERE status = 'PUBLISHED'
			        <if test="keyword != null and keyword != ''">
			            AND (title LIKE CONCAT('%', #{keyword}, '%') OR description LIKE CONCAT('%', #{keyword}, '%'))
			        </if>
			        <if test="category != null and category != ''">
			            AND category = #{category}
			        </if>
			        <choose>
			            <when test="sort == 'popular'">
			                ORDER BY view_count DESC
			            </when>
			            <otherwise>
			                ORDER BY created_at DESC
			            </otherwise>
			        </choose>
			    </script>
			""")
	List<LectureDTO> searchLectureList(@Param("keyword") String keyword, @Param("category") String category,
			@Param("sort") String sort);

	@Select({ "<script>", "SELECT l.*, IFNULL(AVG(ui.rating), 0) AS avgRating, COUNT(ui.interaction_id) AS reviewCount",
			"FROM lectures l",
			"LEFT JOIN user_interactions ui ON l.lecture_id = ui.target_id AND ui.target_type = 'LECTURE'", "<where>",
			" l.status = 'PUBLISHED'", " <if test='keyword != null and keyword != \"\"'>",
			"   AND (l.title LIKE CONCAT('%', #{keyword}, '%')",
			"        OR l.description LIKE CONCAT('%', #{keyword}, '%')",
			"        OR EXISTS (SELECT 1 FROM users u WHERE u.user_id = l.instructor_id AND u.name LIKE CONCAT('%', #{keyword}, '%'))",
			"   )", " </if>", " <if test='category != null and category != \"\"'>", "   AND l.category = #{category}",
			" </if>", "</where>", "GROUP BY l.lecture_id", "<choose>", " <when test='sort == \"popular\"'>",
			"   ORDER BY (AVG(ui.rating) * 0.7 + COUNT(ui.interaction_id) * 0.3) DESC", " </when>", " <otherwise>",
			"   ORDER BY l.published_at DESC", " </otherwise>", "</choose>", "</script>" })
	List<LectureDTO> selectLectures(@Param("keyword") String keyword, @Param("category") String category,
			@Param("sort") String sort);

	@SelectProvider(type = LectureSqlProvider.class, method = "findLectures")
	List<LectureCardDTO> findLectures(LectureSearchCondition cond);

	@SelectProvider(type = LectureSqlProvider.class, method = "countLectures")
	int countLectures(LectureSearchCondition cond);

	@Select("""
			    SELECT lecture_id, title, avg_rating, (
			      SELECT COUNT(*) FROM user_interactions
			      WHERE target_type = 'LECTURE' AND target_id = l.lecture_id
			    ) AS reviewCount
			    FROM lectures l
			    WHERE status = 'PUBLISHED' AND avg_rating IS NOT NULL
			    ORDER BY avg_rating DESC
			    LIMIT #{limit}
			""")
	List<LectureCardDTO> getRecommendedLectures(@Param("limit") int limit);

	@SelectProvider(type = LectureSqlBuilder.class, method = "buildSearchQuery")
	List<LectureDTO> searchLectures(@Param("category") String category, @Param("keyword") String keyword,
			@Param("sort") String sort, @Param("offset") int offset, @Param("size") int size);

	@Insert("""
			    INSERT INTO lectures (title, description, thumbnail, price, instructor_id)
			    VALUES (#{title}, #{description}, #{thumbnail}, #{price}, #{instructorId})
			""")
	@Options(useGeneratedKeys = true, keyProperty = "lectureId")
	void insertLecture(LectureDTO lecture);

	@Insert("""
			    INSERT INTO lecture_contents (lecture_id, title, url, type, order_no, duration)
			    VALUES (#{lectureId}, #{title}, #{url}, #{type}, #{orderNo}, #{duration})
			""")
	void insertContent(ContentDTO content);

	@Select("""
			    SELECT
			        lecture_id,
			        title,
			        avg_rating,
			        description,
			        thumbnail,
			        category,
			        (
			            SELECT COUNT(*)
			            FROM user_interactions
			            WHERE target_type = 'LECTURE' AND target_id = l.lecture_id
			        ) AS reviewCount
			    FROM lectures l
			    WHERE status = 'PUBLISHED' AND avg_rating IS NOT NULL
			    ORDER BY avg_rating DESC
			    LIMIT #{limit}
			""")
	List<LectureDTO> getTopLectures(int limit);

	
	// 내가 등록한 강의 목록
	@Select("""
			    SELECT *
			    FROM lectures
			    WHERE instructor_id = #{userId}
			""")
	List<LectureDTO> getMyLectures(@Param("userId") int userId);

	
	// 내가 신청한 강의 목록
	@Select("""
			    SELECT l.*,
			    e.avg_progress AS avgProgress
			    FROM enrollments e
			    JOIN lectures l ON e.lecture_id = l.lecture_id
			    WHERE e.user_id = #{userId}
			""")
	List<LectureDTO> getMyCourses(int userId);

}
