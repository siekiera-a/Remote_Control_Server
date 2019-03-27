package org.remoteserver.exceptions;

public class RobotException extends Exception {

    public enum ErrorCode {
        NOT_ALLOWED, NO_PERMISSION
    }

    private ErrorCode code;

    public RobotException(String message, ErrorCode code)
    {
        super(message);
        this.code = code;
    }

    public ErrorCode getErrorCode() {
        return code;
    }

}
