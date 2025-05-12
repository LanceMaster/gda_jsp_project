package model.mapper;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import dto.UserDTO;

public interface MainMapper {

	// 로그인	
	@Select("SELECT"
			+ " user_id, "
			+ "email, password, name, phone, birthdate, image_url, role_code, is_verified, is_deleted, agreed_terms, created_at, "
			+ "updated_at, last_login_at, resume_url"
			+ " FROM pks.users WHERE name = '홍길동'")
	UserDTO login(@Param("id")String id,@Param("pw") String pw);
	
	

}
