package hello.jdbc.exception.basic;

import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;

/*
* 예외는 체크 예외(컴파일), 언체크 예외(런타임) 두 가지로 되어 있고
* 잡거나(try catch), 또는 던질(throw) 수 있다.
* 특히 체크 예외를 다룰 때 던지려면 컴파일러가 알 수 있도록 메소드 레벨에서 선언해줘야한다.
* */
@Slf4j
public class CheckedTest {

    @Test
    void checkedCatch(){
        Service service = new Service();
        service.callCatch();
    }

    // 똑같이 throws를 쓰면 에러남, assertThatThrownBy 사용하여 확인
    @Test
    void checkedThrow(){
        Service service = new Service();

        assertThatThrownBy(() -> service.callThrow())
                .isInstanceOf(MyCheckedException.class);
    }

    /**
     * Exception을 상속받은 예외는 체크 예외가 됨(컴파일 체크 예외)
     */
    static class MyCheckedException extends Exception {
        public MyCheckedException(String message) {
            super(message);
        }
    }

    /**
     * Checked 예외는,
     * 1. 잡아서 처리하거나 2. 던지거나 둘 중 하나는 필수로 선택해야 한다.
     */
    static class Service {
        Repository repository = new Repository();

        /**
         * 예외를 잡아서 처리하는 방법
         */
        public void callCatch(){
            try {
                repository.call();
                // 여기 catch에서 Exception을 잡아도 되긴한데, Exception 하위 모든 예외를 잡긴 함
            } catch (MyCheckedException e) {
                // 예외 처리 로직
                log.info("예외처리, message={}", e.getMessage(), e); // 3번째 인자(e) : stack trace
            }
        }

        /**
         * 체크 예외를 밖으로 던지는 방법
         * 예외를 잡지 않고 밖으로 던지려면 throws 예외를 메소드에 필수로 선언해야한다.
         * @throws MyCheckedException
         */
        public void callThrow() throws MyCheckedException {
            repository.call();
        }

    }

    static class Repository {
        // 체크 예외를 다룰 때 예외를 던지려면 컴파일러가 알 수 있도록 메소드 레벨에서 선언해줘야한다.
        public void call() throws MyCheckedException {
            throw new MyCheckedException("리포지토리 예외 발생");
        }

    }

}
