package utils;

import java.io.InputStream;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;

/**
 * 📌 MyBatisUtil
 * - MyBatis SqlSessionFactory를 싱글톤으로 생성하고 외부에 제공하는 유틸리티 클래스
 * - 최초 1회만 설정 파일을 읽어 factory를 생성하고 재사용
 */
public class MyBatisUtil {

    // 💡 애플리케이션 전체에서 공유할 SqlSessionFactory 인스턴스 (싱글톤)
    private static SqlSessionFactory sqlSessionFactory;

    static {
        try {
            // ✅ mybatis-config.xml 파일의 클래스패스 경로
            String resource = "model/mapper/mybatis-config.xml";

            // 📦 설정 파일 로드
            InputStream inputStream = Resources.getResourceAsStream(resource);

            // 🏗 SqlSessionFactory 생성
            sqlSessionFactory = new SqlSessionFactoryBuilder().build(inputStream);

        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("❌ SqlSessionFactory 생성 실패: " + e.getMessage());
        }
    }

    /**
     * ✅ SqlSessionFactory 인스턴스 반환
     * @return SqlSessionFactory
     */
    public static SqlSessionFactory getSqlSessionFactory() {
        return sqlSessionFactory;
    }
}
