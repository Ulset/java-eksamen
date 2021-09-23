package no.kristiania.db;

public class Member {
    private int memberId;
    private String fullName;
    private String email;

    //This is just a member class for each member that will be added to our project.
    public Member(int memberId, String fullName, String email) {
        this.fullName = fullName;
        this.email = email;
        this.memberId = memberId;
    }

    public String getFullName() {
        return fullName;
    }

    public String getEmail() {
        return email;
    }

    public int getMemberId() {
        return this.memberId;
    }
}
