package movlit.be.common.exception;

public class MemberGenreNotFoundException extends ResourceNotFoundException{
    public MemberGenreNotFoundException() {
        super(ErrorMessage.MEMBER_GENRE_NOT_FOUND);
    }

}
