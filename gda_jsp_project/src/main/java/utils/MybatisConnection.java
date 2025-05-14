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
    private static ThreadLocal<SqlSession> sessionLocal = new ThreadLocal<>();

    static {
        String resource = "model/mapper/mybatis-config.xml";
        try (InputStream input = Resources.getResourceAsStream(resource)) {
            sqlMap = new SqlSessionFactoryBuilder().build(input);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static SqlSession getConnection() {
        SqlSession session = sessionLocal.get();
        if (session == null) {
            session = sqlMap.openSession();
            sessionLocal.set(session);
        }
        return session;
    }

    public static void close(SqlSession session) {
        if (session != null) {
            session.commit();
            session.close();
            sessionLocal.remove();
        }
    }
}
