package model.mapper;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import model.dto.NaverUserDTO;

public interface NaverMapper {

    /*
     * 소셜 로그인 시 사용자의 정보를 조회하는 메서드
     */
    @Select("SELECT * FROM social_users WHERE social_id = #{socialId} AND social_type = #{socialType}")    
    NaverUserDTO findBySocialId(@Param("socialId") String socialId, @Param("socialType") String socialType);

    /*
     * 신규 사용자를 등록하는 메서드
     */
    @Insert("""
        INSERT INTO social_users (
            social_id, social_type, email, name, nickname, profile_image
        ) VALUES (
            #{socialId}, #{socialType}, #{email}, #{name}, #{nickname}, #{profileImage}
        )
    """)
    int insertNewUser(NaverUserDTO newUser);
}
