package db;

import no.kristiania.RandomThings;
import no.kristiania.db.Member;
import no.kristiania.db.MemberDao;
import org.flywaydb.core.Flyway;
import org.h2.jdbcx.JdbcDataSource;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.sql.DataSource;
import java.sql.SQLException;
import java.util.ArrayList;

import static org.assertj.core.api.Assertions.assertThat;

public class MemberDaoTest {

    private MemberDao memberDao;


    @BeforeEach
    void setUp(){
        JdbcDataSource dataSource = new JdbcDataSource();
        dataSource.setUrl("jdbc:h2:mem:test;DB_CLOSE_DELAY=-1");
        Flyway.configure().dataSource(dataSource).load().migrate();
        memberDao = new MemberDao(dataSource);
    }

    private static DataSource getTestDatabase() {
        JdbcDataSource dataSource = new JdbcDataSource();
        dataSource.setUrl("jdbc:h2:mem:test;DB_CLOSE_DELAY=-1");
        Flyway.configure().dataSource(dataSource).load().migrate();
        return dataSource;
    }

    public static Member getTestMember() throws SQLException {
        MemberDao db = new MemberDao(getTestDatabase());
        RandomThings randomThings = new RandomThings();
        String name = randomThings.getRandomName(10);
        String email = randomThings.getRandomEmail();
        db.insertUser(name, email);
        return db.getMember(name);
    }

    @Test
    void shouldRetrieveFullNameFromDb() throws SQLException {
        DataSource dataSource = getTestDatabase();
        MemberDao database = new MemberDao(dataSource);
        Member testMember = getTestMember();
        database.insertUser(testMember.getFullName(), testMember.getEmail());
        ArrayList<Member> allMembers = database.getAllMembers();

        ArrayList<String> allNames = new ArrayList<>();
        for(Member memberEl: allMembers){
            allNames.add(memberEl.getFullName());
        }

        assertThat(allNames).contains(testMember.getFullName());
    }

    @Test
    void shouldRetrieveEmailFromDb() throws SQLException {
        DataSource dataSource = getTestDatabase();
        MemberDao database = new MemberDao(dataSource);
        Member testMember = getTestMember();
        database.insertUser(testMember.getFullName(), testMember.getEmail());
        ArrayList<Member> allMembers = database.getAllMembers();

        ArrayList<String> allEmails = new ArrayList<>();
        for(Member memberEl: allMembers){
            allEmails.add(memberEl.getEmail());
        }

        assertThat(allEmails).contains(testMember.getEmail());
    }

    @Test
    void shouldUpdateMemberName() throws SQLException {
        RandomThings randomInator = new RandomThings();
        String randomName = randomInator.getRandomName(10);
        String email = randomInator.getRandomEmail();

        memberDao.insertUser(randomName, email);
        Member member = memberDao.getMember(randomName);

        int memberId = member.getMemberId();
        String newName = randomInator.getRandomName(8);
        memberDao.changeName(member, newName);

        member = memberDao.getMember(newName);

        assertThat(memberId).isEqualTo(member.getMemberId());
    }
}
