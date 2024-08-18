package hello.jdbc.exception.basic;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import java.net.ConnectException;
import java.sql.SQLException;

import static org.assertj.core.api.Assertions.*;

/**
 * 체크 예외 활용
 * 가급적 런타임 예외를 사용하지만 의도적으로 명시할 때, 반드시 처리해야할 예외가 있을 때 쓰임
 * 서비스, 컨트롤러에서 복구 불가능한, 처리할 수 없는 곳에서 throws 를 명시적으로 해야하고
 * 이로 인해 의존성이 생긴다.
 */
public class CheckedAppTest {

    @Test
    void checked() {
        Controller controller = new Controller();
        assertThatThrownBy(() -> controller.request())
                .isInstanceOf(Exception.class);
    }

    static class Controller {
        Service service = new Service();

        // 컨트롤러에서도 throws
        public void request() throws SQLException, ConnectException {
            service.logic();
        }
    }

    static class Service {
        Repository repository = new Repository();
        NetworkClient networkClient = new NetworkClient();

        // 서비스에서도 throws
        public void logic() throws SQLException, ConnectException {
            repository.call();
            networkClient.call();
        }

    }

    static class NetworkClient {
        public void call() throws ConnectException {
            throw new ConnectException("연결 실패");
        }
    }
    static class Repository {
        public void call() throws SQLException {
            throw new SQLException("ex");
        }
    }
}
