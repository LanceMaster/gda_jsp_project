package dao;

import org.apache.ibatis.session.SqlSession;

import dto.UserDTO;
import model.MybatisConnection;
import model.mapper.MainMapper;

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
