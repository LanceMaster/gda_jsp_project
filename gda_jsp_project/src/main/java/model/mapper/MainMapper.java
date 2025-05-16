package model.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import model.dto.UserDTO;

public interface MainMapper {



	//로그인
	@Select("SELECT * FROM users WHERE email = #{id} AND password = #{pw}")
	UserDTO login(@Param("id") String id, @Param("pw") String pw);

	//이메일 중복확인
	@Select("SELECT COUNT(*) FROM users WHERE email = #{email}")
	int emailDupCheck(@Param("email") String email);

	//회원가입
	@Insert("INSERT INTO users (email, password, name, phone) VALUES (#{email}, #{password}, #{name}, #{phone})")	
	int signup( UserDTO userDTO);

	//비밀번호 변경
	@Update("UPDATE users SET password = #{newPassword} WHERE email = #{email}")
	int updatePasswordByEmail(@Param("email") String email, @Param("newPassword") String newPassword);

	//회원탈퇴
	@Delete("DELETE FROM users WHERE email = #{email}")	
	int deleteAccount(String email);



}
