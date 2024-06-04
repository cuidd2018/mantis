package com.u002.core.exception;

/**
 * wrapper client exception.
 *
 * @author amber
 */
public class MantisFrameworkException extends MantisAbstractException {
    private static final long serialVersionUID = -1638857395789735293L;

    public MantisFrameworkException() {
        super(MantisErrorMsgConstant.FRAMEWORK_DEFAULT_ERROR);
    }

    public MantisFrameworkException(MantisErrorMsg motanErrorMsg) {
        super(motanErrorMsg);
    }

    public MantisFrameworkException(String message) {
        super(message, MantisErrorMsgConstant.FRAMEWORK_DEFAULT_ERROR);
    }

    public MantisFrameworkException(String message, MantisErrorMsg motanErrorMsg) {
        super(message, motanErrorMsg);
    }

    public MantisFrameworkException(String message, Throwable cause) {
        super(message, cause, MantisErrorMsgConstant.FRAMEWORK_DEFAULT_ERROR);
    }

    public MantisFrameworkException(String message, Throwable cause, MantisErrorMsg motanErrorMsg) {
        super(message, cause, motanErrorMsg);
    }

    public MantisFrameworkException(Throwable cause) {
        super(cause, MantisErrorMsgConstant.FRAMEWORK_DEFAULT_ERROR);
    }

    public MantisFrameworkException(Throwable cause, MantisErrorMsg motanErrorMsg) {
        super(cause, motanErrorMsg);
    }

}
