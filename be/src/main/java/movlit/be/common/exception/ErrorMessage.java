package movlit.be.common.exception;

public enum ErrorMessage {
    // global
    INVALID_INPUT_VALUE("입력값이 올바르지 않습니다.", "g001"),
    INVALID_ID(" 유효하지 않은 아이디입니다.", "g002"),
    CREATE_TIME_IS_NULL("생성시간이 null이면 안된다", "g003"),
    AUTH_HEADER_NOT_FOUND("요청을 처리하기 위해 필요한 인증 헤더가 존재하지 않습니다", "g004"),
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
    MEMBER_GENRE_NOT_FOUND("멤버의 취향 장르가 존재하지 않습니다.", "m009"),
    IMAGE_ALREADY_EXISTS_IN_MEMBER("해당 멤버에 프로필 이미지가 이미 존재합니다.", "m010"),

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
    MOVIE_CREW_NOT_FOUND("해당 movieId에 해당하는 crew를 찾을 수 없습니다.", "m102"),
    MOVIE_COMMENT_AND_MEMBER_NOT_MATCHED("해당 영화 코멘트와 멤버가 매칭되지 않습니다.", "m103"),
    MOVIE_COMMENT_NOT_FOUND("해당 영화 코멘트를 찾을 수 없습니다.", "m104"),
    MEMBER_EXISTS_IN_MOVIE_COMMENT("해당 영화 코멘트에 현재 멤버가 이미 존재합니다.", "m105"),
    MOVIE_HEART_ALREADY_EXISTS("현재의 아이디가 이미 좋아요 한 영화입니다.", "m106"),
    MOVIE_HEART_NOT_FOUND("해당 영화의 찜을 찾을 수 없습니다.", "m107"),
    MOVIE_COMMENT_LIKE_ALREADY_EXISTS("현재의 아이디가 이미 좋아요 한 코멘트입니다.", "m108"),
    MOVIE_COMMENT_LIKE_NOT_FOUND("해당 코멘트의 좋아요를 찾을 수 없습니다.", "m109"),
    NOT_FOUND_MOVIE_HEART_BY_MEMBER("해당 유저가 찜한 영화가 존재하지 않습니다.", "m110"),

    // Genre
    INVALID_GENRE_ID("해당 장르 ID는 유효하지 않습니다.", "g001"),
    BOOKS_BY_GENRE_NOT_FOUND("해당 장르를 가진 bookEntity가 존재하지 않습니다.", "g002"),

    // book
    BESTSELLERS_NOT_FOUND("bestseller가 존재하지 않습니다.", "b001"),
    BOOKNEW_NOT_FOUND("bookNew가 존재하지 않습니다.", "b002"),
    BOOKNEWSPECIAL_NOT_FOUND("bookNewSpecial이 존재하지 않습니다", "b003"),
    UNKNOWN_BOOK_ENTITY_TYPE("알 수 없는 BookEntity 타입입니다.", "b004"),
    NOT_FOUND_BOOK_HEART_BY_MEMBER("해당 유저가 찜한 도서가 존재하지 않습니다.", "b005"),
    BOOK_NOT_FOUND("해당 도서가 존재하지 않습니다.", "b010"),
    BOOK_CREW_NOT_FOUND("해당 도서의 작가 정보가 존재하지 않습니다", "b012"),
    BOOK_COMMENT_NOT_FOUND("해당 도서 리뷰가 존재하지 않습니다.", "b020"),
    BOOK_COMMENT_AccessDenied("해당 도서 리뷰 접근 권한이 없습니다.", "b021"),

    // group chat
    GROUPCHATROOM_NOT_FOUND("해당 그룹 채팅이 존재하지 않습니다.", "gc01"),
    GROUPCHATROOM_ALREADY_EXISTS("해당 컨텐츠의 그룹 채팅이 이미 존재합니다.", "gc02"),
    GROUPCHATROOM_ASYNC_ERROR("해당 컨텐츠의 그룹 채팅을 비동기적으로 생성하던 도중 오류가 발생하였습니다.", "gc03"),

    // chat
    CONTENT_TYPE_NOT_EXIST("movie와 book 중 그 어느 타입에도 해당하지 않습니다", "c001"),
    CHATROOM_NOT_FOUND("해당 채팅방이 존재하지 않습니다.", "c010"),
    CHATROOM_ACCESS_DENIED("해당 채팅방 접근 권한이 없습니다.", "c011"),

    // Redis
    FAILED_DESERIALIZE_DATA("Redis 데이터를 역직렬화하는데 실패했습니다.", "r001"),
    REDIS_STREAM_OPERATION_RETURN_NULL("Redis Stream 연산 중 NULL 발생했습니다. Redis 연결 및 스트림 상태를 확인해주세요.", "r002");
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
