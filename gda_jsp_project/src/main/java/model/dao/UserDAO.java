package model.dao;

import org.apache.ibatis.session.SqlSession;

import model.dto.UserDTO;
import model.mapper.MainMapper;
import utils.MybatisConnection;

public class UserDAO {
	private Class<MainMapper> mapperClass = MainMapper.class;

	public UserDTO login(String id, String pw) {

    	SqlSession session = MybatisConnection.getConnection();
    	try {
			return session.getMapper(mapperClass).login(id, pw);
		} catch (Exception e) {
			// TODO: handle exception
		}

    	return null;
    			
	}

   

}
