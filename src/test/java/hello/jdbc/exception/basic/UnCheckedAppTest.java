package hello.jdbc.exception.basic;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

import java.net.ConnectException;
import java.sql.SQLException;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

/**
 * 언체크 예외 활용
 * 체크 예외에서 있던 문제, throws 명시적 사용, 의존성 발생 등등을 해결함
 * 어차피 해결 못할거 자동으로 throws 해버리는 것이 트렌드
 * 여기서는 체크 예외 발생 시 런타임 예외로 전환하는 방법 을 사용!!!
 * 공통으로 처리할 필터, 서블릿, 인터셉터에서 반환을 고민하는 것이 더 나음
 */
@Slf4j
public class UnCheckedAppTest {

    @Test
    void unchecked() {
        Controller controller = new Controller();
        assertThatThrownBy(() -> controller.request())
                .isInstanceOf(Exception.class);
    }

    @Test
    void printEx() {
        Controller controller = new Controller();
        try {
            controller.request();
        } catch (Exception e) {
//            e.printStackTrace();
            log.info("ex", e);
        }

    }

    static class Controller {
        Service service = new Service();

        public void request() {
            service.logic();
        }
    }

    static class Service {
        Repository repository = new Repository();
        NetworkClient networkClient = new NetworkClient();

        public void logic() {
            repository.call();
            networkClient.call();
        }

    }

    static class NetworkClient {
        public void call() {
            throw new RuntimeConnectException("연결 실패");
        }
    }

    static class Repository {

        // 체크 예외 (SQLException)을 언체크 예외로 전환 시킴, 이 때 기존 예외도 같이 넘김
        public void call() {
            try {
                runSQL();
            } catch (SQLException e) {
                // 기존 예외를 꼭!! 포함해주어야 예외 출력 시 스택 트레이스에서 기존 예외도 함께 확인할 수 있다
                throw new RuntimeSQLException(e);
            }
        }

        public void runSQL () throws SQLException {
            throw new SQLException("ex");
        }
    }

    static class RuntimeConnectException extends RuntimeException {
        public RuntimeConnectException(String message) {
            super(message);
        }
    }

    static class RuntimeSQLException extends RuntimeException {

        /**
         * 기존 예외를 꼭!! 포함해주어야 예외 출력 시 스택 트레이스에서 기존 예외도 함께 확인할 수 있다
         * @param cause
         */
        public RuntimeSQLException(Throwable cause) {
            super(cause);
        }
    }


}
