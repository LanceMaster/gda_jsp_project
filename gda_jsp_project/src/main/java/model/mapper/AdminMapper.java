package model.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Select;

import model.dto.UserDTO;

public interface AdminMapper {
	
	//전체 사용자 목록 조회
	@Select("SELECT * FROM users")
	List<UserDTO> userList();
	

}
