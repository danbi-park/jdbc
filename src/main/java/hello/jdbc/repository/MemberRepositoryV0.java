package hello.jdbc.repository;

import hello.jdbc.domain.Member;
import lombok.extern.slf4j.Slf4j;

import java.sql.*;
import java.util.NoSuchElementException;

import static hello.jdbc.connection.DBConnectionUtil.*;

@Slf4j
public class MemberRepositoryV0 {
    public Member save(Member member) throws SQLException {
        String sql = "insert into member(member_id, money) values(?, ?)";

        Connection con = null;
        // 파라미터 바인딩 해주는 기능, ? 를 통해 가능하도록 해준다.
        // SQL Injection 공격을 예방하려면 pstmt를 통한 파라미터 바인딩 방식을 사용해야한다.
        PreparedStatement pstmt = null;


        try {
            con = getConnection();
            pstmt = con.prepareStatement(sql); // 데이터베이스에 전달할 sql과 파라미터로 전달할 데이터를 준비

            // 바인딩, 타입도 같이 전달
            pstmt.setString(1, member.getMemberId());
            pstmt.setInt(2, member.getMoney());

            // 쿼리 실행
            pstmt.executeUpdate(); // 참고로 update는 숫자를 영향받은 row 수 만큼 int를 반환한다.
            return member;
        } catch (SQLException e) {
            log.error("db error", e);
            throw e;
        } finally {
            // 사용했던 리소스를 정리해줘야 함
            // 꼭 닫아줘야 함
            /*
            pstmt.close(); // Exception 이 터진다면 여기서 바깥으로 나가서 아래 con.close()를 못할 수 있음, 둘 다 아래 close() 함수 처럼 trycatch 해줘야함
            con.close();
             */
            close(con,pstmt, null);
        }

    }

    // 조회 쿼리
    public Member findById(String memberId) throws SQLException {
        String sql = "select * from member where member_id = ?";

        Connection con = null;
        PreparedStatement pstmt =null;
        ResultSet rs = null; // 결과

        try {
            con = getConnection();
            pstmt = con.prepareStatement(sql);
            pstmt.setString(1, memberId);

            rs = pstmt.executeQuery(); // select 쿼리임
            if(rs.next()){ //내부의 커서가 있는데 한 번은 실행은 해줘야 실제 데이터가 있는 것부터 시작함, 있는지 확인하는 것임
                Member member = new Member();
                member.setMemberId(rs.getString("member_id"));
                member.setMoney(rs.getInt("money"));
                return member;
            } else {
                throw new NoSuchElementException("member not found memberId = "  +memberId); //TEST 확인
            }
        } catch (SQLException e) {
            log.error("db error", e);
            throw  e;
        } finally {
            close(con, pstmt, rs);
        }
    }

    public void update(String memberId, int money) throws SQLException {
        String sql = "update member set money=? where member_id= ?";

        Connection con = null;
        PreparedStatement pstmt = null;

        try {
            con = getConnection();
            pstmt = con.prepareStatement(sql);
            pstmt.setInt(1, money);
            pstmt.setString(2, memberId);

            int resultSize = pstmt.executeUpdate();
            log.info("resultSize={}", resultSize);
        } catch (SQLException e) {
            log.error("db error", e);
            throw  e;
        } finally {
            close(con, pstmt, null);
        }
    }

    public void delete(String memberId) throws SQLException {
        String sql = "delete member where member_id= ?";

        Connection con = null;
        PreparedStatement pstmt = null;

        try {
            con = getConnection();
            pstmt = con.prepareStatement(sql);
            pstmt.setString(1, memberId);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            log.error("db error", e);
            throw  e;
        } finally {
            close(con, pstmt, null);
        }
    }


    // 리소스 정리..
    // connection을 획득하고 connnection을 통해 preparedstatement를 만들었기 때문에 리소스를 반환할 때는 역순으로 종료시켜야한다.
    private void close(Connection con, Statement stmt, ResultSet rs)  {
        if(rs != null){
            try {
                rs.close();
            } catch (SQLException e){
                log.error("error" ,e );
            }
        }

        if(stmt != null){
            try {
                stmt.close();
            } catch (SQLException e){
                log.error("error" ,e );
            }
        }

        if(con != null){
            try {
                con.close();
            } catch (SQLException e){
                log.error("error" ,e );
            }
        }
    }
}
