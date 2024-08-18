package hello.jdbc.exception.basic;

import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

@Slf4j
public class UncheckedTest {

    @Test
    void uncheckedCatch(){
        Service service = new Service();

        service.callCatch();
    }

    @Test
    void uncheckedThrow(){
        Service service = new Service();
        Assertions.assertThatThrownBy(()-> service.callThrow())
                .isInstanceOf(MyUncheckedException.class);
    }


    /**
     * RuntimeException을 상속 받은 예외는 언체크 예외가 된다.
     */
    static class MyUncheckedException extends RuntimeException {
        public MyUncheckedException(String message) {
            super(message);
        }
    }

    /**
     * Unchecked 얘외는
     * 예외를 잡거나, 던지지 않아도 된다.
     * 예외를 잡지 않으면 자동으로 밖으로 던진다.
     */
    static class Service {
        Repository repository = new Repository();

        /**
         * 필요한 경우 예외 잡아서 처리 가능!! 체크, 언체크는 그냥 컴파일러가 확인 하냐 안하냐 차이임
         */
        public void callCatch(){
            try {
                repository.call();
            } catch (MyUncheckedException e){
                log.info("언체크 예외 발생, message={}", e.getMessage(), e);
            }
        }

        /**
         * 예외를 잡지 않아도 자동으로 상위 호출 스택으로 넘어간다.
         * 체크 예외와 다르게 throws 예외 선언을 하지 않아도 된다.
         */
        public void callThrow(){
            repository.call();
        }

    }


    static class Repository {
        // 예외를 throw 해도 메소드에 따로 throws를 생략이 가능하다.
        public void call(){
            throw new MyUncheckedException("언체크 예외 발생");
        }
    }
}
