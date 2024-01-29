package cleanarchitecture.domain.common.ex;

public class ApplicationException extends RuntimeException {

    private final String code;

    public ApplicationException(String message) {
        this(message, null);
    }

    public ApplicationException(String message, String code) {
        super(message);
        this.code = code;
    }

    public String getCode(){
        return code;
    }
}
