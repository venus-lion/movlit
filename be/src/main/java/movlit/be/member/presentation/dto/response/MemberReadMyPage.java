package movlit.be.member.presentation.dto.response;

public record MemberReadMyPage(String profileImgUrl, String nickname, String email,
                               Long movieHeartCount, Long movieCommentCount) {

}
