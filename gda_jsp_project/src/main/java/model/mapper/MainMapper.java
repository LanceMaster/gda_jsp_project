package model.mapper;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import model.dto.UserDTO;

public interface MainMapper {



	//로그인
	@Select("SELECT * FROM users WHERE email = #{id} AND password = #{pw}")
	UserDTO login(@Param("id") String id, @Param("pw") String pw);
}
