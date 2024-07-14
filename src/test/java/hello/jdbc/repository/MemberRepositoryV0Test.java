package hello.jdbc.repository;

import hello.jdbc.domain.Member;
import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import java.sql.SQLException;
import java.util.NoSuchElementException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@Slf4j
class MemberRepositoryV0Test {

    MemberRepositoryV0 repository = new MemberRepositoryV0();


    // crud를 한번에 테스트 하는 건 편하긴 한데, 한 번 예외가 발생하면 다음 작업이 진행이 안된다. 이 부분은 이후 TRANSACTION으로 처리한다.
    @Test
    void crud() throws SQLException {
        // save
        Member member = new Member("danbi101", 100000);
        repository.save(member);

        // findById
        Member findMember = repository.findById(member.getMemberId());
        log.info("findMember ={}", findMember);
        log.info("member != findMember : {}", member == findMember); // 다른 인스턴스라서 false
        log.info("member equals findMember : {}", member.equals(findMember)); // lombok @Data에서는 equalsandhashcode를 포함하기 때문에 true로 반환되는 것임
//        assertThat(findMember).isEqualTo(member);

        //update
        repository.update(member.getMemberId(), 20000);
        Member updatedMember = repository.findById(member.getMemberId());
        assertThat(updatedMember.getMoney()).isEqualTo(20000);


        //delete
        repository.delete(member.getMemberId());
        assertThatThrownBy(() -> repository.findById(member.getMemberId())).isInstanceOf(NoSuchElementException.class);
    }
}