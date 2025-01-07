package movlit.be.common.exception;

public class ImageAlreadyExistsInMemberException  extends ResourceNotFoundException{

    public ImageAlreadyExistsInMemberException() {
        super(ErrorMessage.IMAGE_ALREADY_EXISTS_IN_MEMBER);
    }

}
