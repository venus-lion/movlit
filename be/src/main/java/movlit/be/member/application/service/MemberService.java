package movlit.be.member.application.service;

import java.util.List;
import movlit.be.Member;

public interface MemberService   {
    public static final int CORRECT_LOGIN = 0;
    public static final int WRONG_PASSWORD = 1;
    public static final int USER_NOT_EXIST = 2;

    Member findByUid(String uid);

    List<Member> getUsers();

    void registerUser(Member user);

    void updateUser(Member user);

    void deleteUser(String uid);

    int login(String uid, String pwd);
}
