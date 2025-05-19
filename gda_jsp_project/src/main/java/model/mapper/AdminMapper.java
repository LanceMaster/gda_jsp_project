package model.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Select;

import model.dto.UserDTO;

public interface AdminMapper {
	
	//전체 사용자 목록 조회
	@Select("SELECT * FROM users WHERE role = 'STUDENT' AND is_deleted = 0")
	List<UserDTO> userList();

	// 사용자 ID로 사용자 정보 조회 (수강생만 조
	@Select("SELECT * FROM users WHERE user_id = #{userId}")
	UserDTO getUserById(String userId);

	// 사용자 삭제
	@Delete("DELETE FROM users WHERE user_id = #{userId}")
	int deleteUser(String userId);

	// 사용자 이름으로 검색
	@Select("SELECT * FROM users WHERE user_name LIKE CONCAT('%', #{keyword}, '%') AND role = 'STUDENT' AND is_deleted = 0")
	List<UserDTO> searchUsersByName(String keyword);
	

}
