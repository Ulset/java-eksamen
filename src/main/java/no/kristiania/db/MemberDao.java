package no.kristiania.db;

import javax.sql.DataSource;
import java.io.*;
import java.sql.*;
import java.util.ArrayList;

//This extends the base class and implements some new functionality
public class MemberDao extends BaseDao {
    public MemberDao(DataSource dataSource) {
        super(dataSource);
    }

    public MemberDao() throws IOException {
        super();
    }

    //This is where we insert the new member to our database - this method takes the fullName and email provided from the post request and inserts it into the db
    public void insertUser(String fullName, String email) throws SQLException {
        try (Connection connection = this.getDataSource().getConnection()) {
            try (PreparedStatement statement = connection.prepareStatement("INSERT INTO members (fullName, email) VALUES (?, ?)")) {
                statement.setString(1, fullName);
                statement.setString(2, email);
                statement.executeUpdate();
            }
        }
    }

    //This is our list method, which retrieves the information from the database.
    public ArrayList<Member> getAllMembers() throws SQLException {
        ArrayList<Member> output = new ArrayList<>();
        try (Connection connection = this.getDataSource().getConnection()) {
            try (PreparedStatement statement = connection.prepareStatement("SELECT userid, fullname, email from members")) {
                try (ResultSet rs = statement.executeQuery()) {
                    while (rs.next()) {
                        //As long as the ResultSet has information, we will append this to our index page.
                        Member newMember = new Member(rs.getInt("userId"), rs.getString("fullname"), rs.getString("email"));
                        output.add(newMember);
                    }
                }
            }
        }
        return output;
    }

    public Member getMember(String userInput) throws SQLException {
        try (Connection connection = this.getDataSource().getConnection()) {
            try (PreparedStatement statement = connection.prepareStatement("SELECT userId, fullname, email from members WHERE fullname = ?")) {
                statement.setString(1, userInput);
                try (ResultSet rs = statement.executeQuery()) {
                    while (rs.next()) {
                        //As long as the ResultSet has information, we will append this to our index page.
                        Member newMember = new Member(rs.getInt("userId"), rs.getString("fullname"), rs.getString("email"));
                        return newMember;
                    }
                }
            }
        }
        return null;
    }

    public void changeName(Member currMember, String newName) throws SQLException {
        try (Connection connection = this.getDataSource().getConnection()) {
            try (PreparedStatement statement = connection.prepareStatement("UPDATE members SET fullname = ? WHERE fullname = ?")) {
                statement.setString(1, newName);
                statement.setString(2, currMember.getFullName());
                statement.executeUpdate();
            }
        }
    }
}
