package hello.jdbc.service;

import hello.jdbc.domain.Member;
import hello.jdbc.repository.MemberRepositoryV3;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;

import java.sql.Connection;
import java.sql.SQLException;

/**
 * 트랜잭션 - 트랜잭션 매니저
 * PlatformTransactionManager 사용, -> 추상화, 단일책임원칙
 */
@Slf4j
@RequiredArgsConstructor
public class MemberServiceV3_1 {
    private final PlatformTransactionManager transactionManager; // 여기서 JPA, JDBC 트랜잭션 매니저를 바로 주입 받지 않음.(단일 책임 원칙)
    private final MemberRepositoryV3 memberRepository;

    public void accountTransfer(String fromId, String toId, int money) throws SQLException {
        //트랜잭션 시작(따로 setAutoCommit 필요 x)
        TransactionStatus status = transactionManager.getTransaction(new DefaultTransactionDefinition()); /// 현재 트랜잭션의 상태 정보가 포함되어 있음.

        try {
            //비즈니스 로직
//            bizLogic(con, fromId, toId, money);
            bizLogic(fromId, toId, money); // 이전과 달리 con을 넘길 필요 없음.
            transactionManager.commit(status); //성공시 커밋
        } catch (Exception e) {
            transactionManager.rollback(status); //실패시 롤백
            throw new IllegalStateException(e);
        }
        // 알아서 정리를 해주기 때문에 따로 닫아줄 필요 없음
//        } finally {
//            release(con);
//        }

    }

    private void bizLogic(String fromId, String toId, int money) throws SQLException {
        Member fromMember = memberRepository.findById(fromId);
        Member toMember = memberRepository.findById(toId);

        memberRepository.update(fromId, fromMember.getMoney() - money);
        validation(toMember);
        memberRepository.update(toId, toMember.getMoney() + money);
    }

    private void validation(Member toMember) {
        if (toMember.getMemberId().equals("ex")) {
            throw new IllegalStateException("이체중 예외 발생");
        }
    }

}
