package movlit.be.member.application.service;

import java.util.List;
import movlit.be.Member;

public interface MemberService {

    public static final int CORRECT_LOGIN = 0;
    public static final int WRONG_PASSWORD = 1;
    public static final int Member_NOT_EXIST = 2;

    Member findByMemberId(String memberId);

    List<Member> getMembers();

    void registerMember(Member member);

    void updateMember(Member member);

    void deleteMember(String memberId);

    int login(String memberId, String password);

}
