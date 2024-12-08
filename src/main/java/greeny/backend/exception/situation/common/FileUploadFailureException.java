package greeny.backend.exception.situation.common;

public class FileUploadFailureException extends RuntimeException {
    public FileUploadFailureException(String message) {
        super(message);
    }
}
