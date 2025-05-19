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
	@Insert("""
		    INSERT INTO users (
		        email, password, name, phone, role, resume, agreed_terms, birthdate
		    ) VALUES (
		        #{email}, #{password}, #{name}, #{phone}, #{role}, #{resume}, #{agreedTerms}, #{birthdate}
		    )
		""")
		int signup(UserDTO user);

	//비밀번호 변경
	@Update("UPDATE users SET password = #{newPassword} WHERE email = #{email}")
	int updatePasswordByEmail(@Param("email") String email, @Param("newPassword") String newPassword);

	//회원탈퇴
	@Delete("DELETE FROM users WHERE email = #{email}")	
	int deleteAccount(String email);

	
	
	//비밀번호 찾기
	@Select("SELECT COUNT(*) FROM users WHERE email = #{email} AND name = #{name}")
	int findPassword(@Param("email") String email, @Param("name") String name);

	//아이디 찾기
	@Select("SELECT email FROM users WHERE name = #{name} AND birthdate = #{birthdate}")
	String findId(@Param("name") String name, @Param("birthdate") String birthdate);

	//이력서 파일명 가져오기
	@Select("SELECT resume FROM users WHERE email = #{email}")
	String getResumeFilename(String parameter);

	



}
