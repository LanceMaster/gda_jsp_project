package model.mapper;

import model.dto.ContentDTO;
import model.dto.LectureDTO;
import org.apache.ibatis.annotations.*;

public interface LectureUploadMapper {

    @Insert("""
        INSERT INTO lectures
        (title, description, thumbnail, category, price, status, avg_rating, published_at, created_at, updated_at, instructor_id, curriculum)
        VALUES
        (#{title}, #{description}, #{thumbnail}, #{category}, #{price}, 'PUBLISHED', NULL, NOW(), NOW(), NOW(), #{instructorId}, #{curriculum})
    """)
    @Options(useGeneratedKeys = true, keyProperty = "lectureId")
    void insertLecture(LectureDTO dto);

    @Insert("""
        INSERT INTO lecture_contents
        (lecture_id, title, url, type, order_no, duration, created_at)
        VALUES
        (#{lectureId}, #{title}, #{url}, #{type}, #{orderNo}, #{duration}, NOW())
    """)
    void insertContent(ContentDTO dto);
}
