package utils;

import java.io.IOException;
import java.io.InputStream;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;

public class MybatisConnection {
    private MybatisConnection() {}
    private static SqlSessionFactory sqlMap;

    static {
        String resource = "model/mapper/mybatis-config.xml";
        try (InputStream input = Resources.getResourceAsStream(resource)) {
            sqlMap = new SqlSessionFactoryBuilder().build(input);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static SqlSession getConnection() {
        return sqlMap.openSession();  // ✅ 매번 새로운 세션 반환
    }

    public static void close(SqlSession session) {
        if (session != null) {
            session.commit();
            session.close();
        }
    }
}
