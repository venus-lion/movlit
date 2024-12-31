package movlit.be.auth.application.service;

import lombok.Getter;
import movlit.be.common.util.ids.MemberId;
import movlit.be.member.domain.Member;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;

@Getter
public class MyMemberDetails implements UserDetails, OAuth2User {

    private final Member member;
    private Map<String, Object> attributes; // OAuth2 속성

    // 일반 로그인 생성자
    public MyMemberDetails(Member member) {
        this.member = member;
    }

    // OAuth2 로그인 생성자
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
        collection.add(() -> member.getRole());
        return collection;
    }

    @Override
    public String getPassword() {
        return member.getPassword();
    }

    @Override
    public String getUsername() {
        return member.getEmail(); // 이메일을 사용자 이름으로 사용
    }

    @Override
    public String getName() {
        // OAuth2User 인터페이스를 위한 구현 (일반 로그인에서는 사용되지 않음)
        return (attributes == null) ? null : attributes.get("name").toString();
    }

    // 나머지 메서드들은 그대로 유지
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