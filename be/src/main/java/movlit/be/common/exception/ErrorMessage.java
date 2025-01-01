package movlit.be.common.exception;

public enum ErrorMessage {
    // global
    INVALID_INPUT_VALUE("입력값이 올바르지 않습니다.", "g001"),
    INVALID_ID(" 유효하지 않은 아이디입니다.", "g002"),
    CREATE_TIME_IS_NULL("생성시간이 null이면 안된다", "g003"),
    REQUEST_HEADER_NOT_FOUND("요청을 처리하기 위해 필요한 헤더가 존재하지 않습니다", "g004"),
    PERMISSION_DENIED_ACCESS("권한이 없습니다.", "g005"),

    COUNT_OUT_OF_BOUNDS("카운트가 더 이상 감소할 수 없습니다", null),

    // member
    MEMBER_NOT_FOUND("존재하지 않는 회원입니다", "m001"),
    DUPLICATE_MEMBER_EMAIL("이미 가입된 이메일입니다", "m002"),
    DUPLICATE_MEMBER_NICKNAME("이미 존재하는 닉네임입니다", "m003"),
    INVALID_CONFIRM_PASSWORD("재입력한 비밀번호가 입력한 비밀번호와 일치하지 않습니다", "m004"),
    PASSWORD_PATTERN_NOT_MATCH("비밀번호는 8글자 이상이어야 합니다", "m005"),
    NOT_FOLLOWING("해당 회원을 팔로우하고있지 않습니다", "m006"),
    ALREADY_FOLLOWING("해당 회원을 이미 팔로우하고 있습니다", "m007"),
    INVALID_MEMBER_ID("유효하지 않은 회원 id입니다", "m008"),

    // auth
    UNAUTHORIZED("권한이 없습니다", "a001"),
    INVALID_TOKEN("토큰이 유효하지 않습니다", "a002"),
    CLAIM_NOT_FOUND("토큰에 해당 클레임이 존재하지 않습니다", "a003"),
    INVALID_ACCESS_TOKEN("유효하지 않은 액세스 토큰입니다", "a004"),
    MEMBER_INCORRECT_PASSWORD("사용자 정보와 비밀번호가 일치하지 않습니다", "a005"),
    UNSUPPORTED_OAUTH_PROVIDER("지원되지 않는 OAuth입니다.", "a006"),
    INVALID_OAUTH_RESPONSE("OAuth 클라이언트로부터의 응답이 유효하지 않습니다", "a007"),
    EXPIRED_TOKEN("세션이 만료되었습니다. 다시 로그인해 주세요.", "a008"),


    // movie
    MOVIE_NOT_FOUND("메시지", "m001"),


    // book
    BOOK_NOT_FOUND("해당 도서가 존재하지 않습니다.", "b001"),
    BOOK_CREW_NOT_FOUND("해당 도서의 작가 정보가 존재하지 않습니다", "b002"),
    BOOK_COMMENT_NOT_FOUND("해당 도서 리뷰가 존재하지 않습니다.", "b010"),
    BOOK_COMMENT_AccessDenied("해당 도서 리뷰 접근 권한이 없습니다.", "b011");

    private final String message;
    private final String code;

    ErrorMessage(String message, String code) {
        this.message = message;
        this.code = code;
    }

    public String getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }

}
