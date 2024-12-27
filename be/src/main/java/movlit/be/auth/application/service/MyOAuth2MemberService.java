package movlit.be.auth.application.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Map;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import movlit.be.common.exception.MemberNotFoundException;
import movlit.be.common.exception.MovieNotFoundException;
import movlit.be.common.util.IdFactory;
import movlit.be.member.application.service.MemberReadService;
import movlit.be.member.application.service.MemberWriteService;
import movlit.be.member.domain.Member;
import org.springframework.cglib.core.Local;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class MyOAuth2MemberService extends DefaultOAuth2UserService {

    private final MemberReadService memberReadService;
    private final MemberWriteService memberWriteService;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest memberRequest) throws OAuth2AuthenticationException {
        String email, uname, profileUrl;
        String hashedPwd = bCryptPasswordEncoder.encode("Social Login");
        Member member = null;

        OAuth2User oAuth2User = super.loadUser(memberRequest);
        log.info("===getAttributes()===: " + oAuth2User.getAttributes());

        String provider = memberRequest.getClientRegistration().getRegistrationId();
        switch (provider) {
            case "github":
                // FIXME. 여기서 throw를 던져버리니까 이 OAuth 로직 자체를 수정해야 함
                email = oAuth2User.getAttribute("email");
                try{
                    member = memberReadService.findByMemberEmail(email);
                } catch (MemberNotFoundException e){
                    uname = oAuth2User.getAttribute("name");
                    uname = (uname == null) ? "github_Member" : uname;
                    profileUrl = oAuth2User.getAttribute("avatar_url");
                    member = Member.builder()
                            .memberId(IdFactory.createMemberId()).password(hashedPwd).nickname(uname).email(email)
                            .role("ROLE_Member").provider(provider).profileImgUrl(profileUrl)
                            .regDt(LocalDateTime.now()).updDt(LocalDateTime.now())
                            .build();
                    memberWriteService.registerMember(member);
                    log.info("깃허브 계정을 통해 회원가입이 되었습니다. " + member.getNickname());
                }

                break;

            case "google":
                email = oAuth2User.getAttribute("email");    // Google Email
                try{
                    member = memberReadService.findByMemberEmail(email);

                    log.info("=== findByMemberEmail : {}", member);
                } catch (MemberNotFoundException e){
                    uname = oAuth2User.getAttribute("name");
                    profileUrl = oAuth2User.getAttribute("picture");
                    // TODO : 생일 처리
                    member = Member.builder()
                            .memberId(IdFactory.createMemberId()).password(hashedPwd).nickname(uname).email(email)
                            .role("ROLE_Member").provider(provider).profileImgUrl(profileUrl)
                            .regDt(LocalDateTime.now()).updDt(LocalDateTime.now())
                            .build();

                    memberWriteService.registerMember(member);
                    log.info("구글 계정을 통해 회원가입이 되었습니다. " + member.getNickname());
                }
                break;

            case "naver":
                Map<String, Object> response = (Map) oAuth2User.getAttribute("response");
                email = (String)response.get("email");

                try{
                    member = memberReadService.findByMemberEmail(email);
                } catch (MemberNotFoundException e){
                    uname = Optional.ofNullable((String) response.get("name")).orElse("naver_Member");
                    profileUrl = Optional.ofNullable((String)response.get("profile_image")).orElse("");

                    // 생일 처리
                    Optional<String> birthday = Optional.ofNullable((String)response.get("birthday"));
                    Optional<String> birthyear = Optional.ofNullable((String)response.get("birthyear"));
                    String dob = "";
                    if(birthday.isPresent() && birthyear.isPresent()){
                        dob = birthyear.get() + "-" + birthday.get();
                    }

                    member = Member.builder()
                            .memberId(IdFactory.createMemberId()).password(hashedPwd).nickname(uname).email(email)
                            .role("ROLE_Member").provider(provider).profileImgUrl(profileUrl).dob(dob)
                            .regDt(LocalDateTime.now()).updDt(LocalDateTime.now())
                            .build();

                    memberWriteService.registerMember(member);
                }
                break;

            case "kakao":
                long kid = (long) oAuth2User.getAttribute("id");
//                memberId = provider + "_" + kid;
//                member = memberReadService.findByMemberId(memberId);
//                if (member == null) {         // 내 DB에 없으면 가입을 시켜줌
//                    Map<String, String> properties = (Map) oAuth2User.getAttribute("properties");
//                    Map<String, Object> account = (Map) oAuth2User.getAttribute("kakao_account");
//                    uname = (String) properties.get("nickname");
//                    uname = (uname == null) ? "kakao_Member" : uname;
//                    email = (String) account.get("email");
//                    profileUrl = (String) properties.get("profile_image");
//                    member = Member.builder()
//                            .memberId(memberId).password(hashedPwd).nickname(uname).email(email)
//                            .regDt(LocalDateTime.now()).role("ROLE_Member").provider(provider).profileImgUrl(profileUrl)
//                            .build();
//                    memberWriteService.registerMember(member);
//                    log.info("카카오 계정을 통해 회원가입이 되었습니다. " + member.getNickname());
//                }
                break;

            case "facebook":
                String fid = oAuth2User.getAttribute("id");    // Facebook ID
//                memberId = provider + "_" + fid;
//                member = memberReadService.findByMemberId(memberId);
//                if (member == null) {         // 내 DB에 없으면 가입을 시켜줌
//                    uname = oAuth2User.getAttribute("name");
//                    uname = (uname == null) ? "facebook_Member" : uname;
//                    email = oAuth2User.getAttribute("email");
//                    member = Member.builder()
//                            .memberId(memberId).password(hashedPwd).nickname(uname).email(email)
//                            .regDt(LocalDateTime.now()).role("ROLE_Member").provider(provider)
//                            .build();
//                    memberWriteService.registerMember(member);
//                    log.info("페이스북 계정을 통해 회원가입이 되었습니다. " + member.getNickname());
//                }
                break;
        }

        return new MyMemberDetails(member, oAuth2User.getAttributes());
    }

}
