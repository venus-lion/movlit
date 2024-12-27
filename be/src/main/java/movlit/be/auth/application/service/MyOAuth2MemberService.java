package movlit.be.auth.application.service;

import java.time.LocalDateTime;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import movlit.be.common.exception.MovieNotFoundException;
import movlit.be.common.util.IdFactory;
import movlit.be.member.application.service.MemberReadService;
import movlit.be.member.application.service.MemberWriteService;
import movlit.be.member.domain.Member;
import movlit.be.member.presentation.dto.request.MemberRegisterRequest;
import movlit.be.member.presentation.dto.request.MemberRegisterRequest.MemberRegisterRequestBuilder;
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
        String memberId, email, uname, profileUrl;
        String hashedPwd = bCryptPasswordEncoder.encode("Social Login");
        Member member = null;

        OAuth2User oAuth2User = super.loadUser(memberRequest);
        log.info("===getAttributes()===: " + oAuth2User.getAttributes());

        String provider = memberRequest.getClientRegistration().getRegistrationId();
        switch (provider) {
            case "github":
                email = oAuth2User.getAttribute("email");

                // FIXME. 여기서 throw를 던져버리니까 이 OAuth 로직 자체를 수정해야 함
                member = memberReadService.findByMemberEmail(email);

                if (member == null) {         // 내 DB에 없으면 가입을 시켜줌
                    uname = oAuth2User.getAttribute("name");
                    uname = (uname == null) ? "github_Member" : uname;
                    profileUrl = oAuth2User.getAttribute("avatar_url");
//                    member = Member.builder()
//                            .memberId(IdFactory.createMemberId()).password(hashedPwd).nickname(uname).email(email)
//                            .regDt(LocalDateTime.now()).role("ROLE_Member").provider(provider).profileImgUrl(profileUrl)
//                            .build();
                    // FIXME: OAuth쪽은 register 분리
                    MemberRegisterRequest request = MemberRegisterRequest.builder()
                            .nickname(uname)
                            .email(email)
                            .password(hashedPwd)
                            .build();
                    memberWriteService.registerMember(request);
                    log.info("깃허브 계정을 통해 회원가입이 되었습니다. " + member.getNickname());
                }
                break;

            case "google":
                String sub = oAuth2User.getAttribute("sub");    // Google ID
                memberId = provider + "_" + sub;
//                member = memberReadService.findByMemberId(memberId);
//                if (member == null) {         // 내 DB에 없으면 가입을 시켜줌
//                    uname = oAuth2User.getAttribute("name");
//                    uname = (uname == null) ? "google_Member" : uname;
//                    email = oAuth2User.getAttribute("email");
//                    profileUrl = oAuth2User.getAttribute("picture");
//                    member = Member.builder()
//                            .memberId(memberId).password(hashedPwd).nickname(uname).email(email)
//                            .regDt(LocalDateTime.now()).role("ROLE_Member").provider(provider).profileImgUrl(profileUrl)
//                            .build();
//                    memberWriteService.registerMember(member);
//                    log.info("구글 계정을 통해 회원가입이 되었습니다. " + member.getNickname());
//                }
                break;

            case "naver":
                Map<String, Object> response = (Map) oAuth2User.getAttribute("response");
                String nid = (String) response.get("id");
                memberId = provider + "_" + nid;
//                member = memberReadService.findByMemberId(memberId);
//                if (member == null) {         // 내 DB에 없으면 가입을 시켜줌
//                    uname = (String) response.get("nickname");
//                    uname = (uname == null) ? "naver_Member" : uname;
//                    email = (String) response.get("email");
//                    profileUrl = (String) response.get("profile_image");
//                    member = Member.builder()
//                            .memberId(memberId).password(hashedPwd).nickname(uname).email(email)
//                            .regDt(LocalDateTime.now()).role("ROLE_Member").provider(provider).profileImgUrl(profileUrl)
//                            .build();
//                    memberWriteService.registerMember(member);
//                    log.info("네이버 계정을 통해 회원가입이 되었습니다. " + member.getNickname());
//                }
                break;

            case "kakao":
                long kid = (long) oAuth2User.getAttribute("id");
                memberId = provider + "_" + kid;
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
                memberId = provider + "_" + fid;
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
