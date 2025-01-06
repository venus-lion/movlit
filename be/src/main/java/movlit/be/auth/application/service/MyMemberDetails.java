package movlit.be.auth.application.service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;
import lombok.Getter;
import movlit.be.common.util.ids.MemberId;
import movlit.be.member.domain.Member;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.user.OAuth2User;

// 스프링 시큐리티가 로그인 포스트 요청을 낚아채서 로그인을 진행시킴
// 로컬 로그인 - MemberDetails 구현
// 소셜 로그인 - OAuth2Member 구현

public class MyMemberDetails implements UserDetails, OAuth2User {

    // 공통
    @Getter
    private MemberId memberId;

    // 로컬 로그인
    @Getter
    private Member member;

    // 소셜 로그인
    private Map<String, Object> attributes;

    public MyMemberDetails(Member member) {
        this.member = member;
        this.memberId = member.getMemberId();
    }

    public MyMemberDetails(Member member, Map<String, Object> attributes) {
        this.member = member;
        this.attributes = attributes;
    }

    @Override
    public Map<String, Object> getAttributes() {
        return attributes;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        Collection<GrantedAuthority> collection = new ArrayList<>();
        collection.add((GrantedAuthority) () -> member.getRole());
        return collection;
    }

    @Override
    public String getPassword() {
        return member.getPassword();
    }

    @Override
    public String getUsername() {
        return member.getEmail();
    }

    @Override
    public String getName() {
        return member.getNickname();
    }

    // 버전 문제로 기존 default 메서드 추가
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

}