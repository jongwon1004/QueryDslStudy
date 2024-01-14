package study.querydsl.entity;

import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.*;
import static study.querydsl.entity.QMember.*;

@SpringBootTest
@Transactional
public class QueryDslBasicTest {

    @Autowired
    EntityManager em;

    JPAQueryFactory queryFactory;

    @BeforeEach // 테스트 실행전 실행됨
    public void before() {
        queryFactory = new JPAQueryFactory(em);

        Team teamA = new Team("teamA");
        Team teamB = new Team("teamB");
        em.persist(teamA);
        em.persist(teamB);

        Member member1 = new Member("member1", 10, teamA);
        Member member2 = new Member("member2", 20, teamA);
        Member member3 = new Member("member3", 30, teamB);
        Member member4 = new Member("member4", 40, teamB);
        em.persist(member1);
        em.persist(member2);
        em.persist(member3);
        em.persist(member4);
    }

    @Test
    public void startJPQL() {
        Member findMember = em.createQuery("select m from Member m where m.username =:username", Member.class)
                .setParameter("username", "member1")
                .getSingleResult();

        assertThat(findMember.getUsername()).isEqualTo("member1");
    }

    @Test
    public void startQueryDsl() {
//        QMember m = new QMember("m");

        Member findMember = queryFactory
                .select(member) // static import 추가
                .from(member)
                .where(member.username.eq("member1")) // PreparedStatement 로 쿼리가 나감, 파라미터 바인딩 처리
                .fetchOne();


        assertThat(findMember.getUsername()).isEqualTo("member1");

    }

    @Test
    public void search() {
        Member findMember = queryFactory
                .selectFrom(member)
                .where(member.username.eq("member1").
                        and(member.age.eq(10)))
                .fetchOne();

        assertThat(findMember.getUsername()).isEqualTo("member1");

        /*
            member.username.eq("member1"); // username = 'member1'
            member.username.ne("member1"); // username != 'member1'
            member.username.eq("member1").not(); // username != 'member1'

            member.username.isNotNull(); // 이름이 is not null

            member.age.in(10, 20); // age in (10,20)
            member.age.notIn(10, 20); // age not in (10, 20)
            member.age.between(10, 30); // between 10, 30

            member.age.goe(30); // age >= 30
            member.age.gt(30); // age > 30
            member.age.loe(30); // age <= 30
            member.age.lt(30); // age < 30

            member.username.like("member%"); // like 검색
            member.username.contains("member"); // like '%member%' 검색
            member.username.startsWith("member"); // like 'member%' 검색

         */


    }

    @Test
    public void searchAndParam() {
        Member findMember = queryFactory
                .selectFrom(member)
                .where(member.username.eq("member1"), member.age.eq(10)) // 메서드 체인 말고 그냥 쉼표로 적어도됨 (and) 인 경우
                .fetchOne();

        // 만약에 밑에 처럼
        /*
            .where(
                member.username.eq("member1"),
                member.age.eq(10),
                null  <-- 이렇게 null 이 추가 되면 그냥 무시함. 이걸 이용해서 기가막힌 동적쿼리를 만들 수 있다
         */

        assertThat(findMember.getUsername()).isEqualTo("member1");
    }
}
