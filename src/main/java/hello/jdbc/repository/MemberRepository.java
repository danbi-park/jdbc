package hello.jdbc.repository;

import hello.jdbc.domain.Member;

/**
 * throws Exception을 하지 않는 interface
 */
public interface MemberRepository {
    Member save(Member member);

    Member findById(String memberId);

    void update(String memberId, int money);

    void delete(String memberId);

}
