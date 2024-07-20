package hello.jdbc.connection;

import lombok.extern.slf4j.Slf4j;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import static hello.jdbc.connection.ConnectionConst.*;

@Slf4j
public class DBConnectionUtil {


    /// 커넥션을 연결하는 방법 1 - DriverManager (좀 구식 ^^)
    public static Connection getConnection(){
        try {
            //jdbc가 제공하는 jdbc 드라이버 매니저가 h2의 Driver가 찾아 실제 커넥션을 가져옴
            Connection connection = DriverManager.getConnection(URL, USERNAME, PASSWORD);
            /// external library의 h2의 /org/h2/jdbc/JdbcConnection.class를 가져오는거임
            log.info("get connection={}, class={}", connection, connection.getClass());
            return connection;
        } catch (SQLException e) {
            throw new IllegalStateException(e);
        }
    }

}
