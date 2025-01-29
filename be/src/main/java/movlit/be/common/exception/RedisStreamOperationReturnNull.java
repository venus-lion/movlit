package movlit.be.common.exception;

public class RedisStreamOperationReturnNull extends ResourceNotFoundException {

    public RedisStreamOperationReturnNull() {
        super(ErrorMessage.REDIS_STREAM_OPERATION_RETURN_NULL);
    }

}
