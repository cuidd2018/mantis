package com.u002.core.exception;

import com.u002.core.rpc.RpcContext;

/**
 * 运行时异常
 *
 * @author amber
 * @version 创建时间：
 */
public abstract class MantisAbstractException extends RuntimeException {
    private static final long serialVersionUID = -8742311167276890503L;

    protected MantisErrorMsg mantisErrorMsg = MantisErrorMsgConstant.FRAMEWORK_DEFAULT_ERROR;
    protected String errorMsg = null;

    public MantisAbstractException() {
        super();
    }

    public MantisAbstractException(MantisErrorMsg mantisErrorMsg) {
        super();
        this.mantisErrorMsg = mantisErrorMsg;
    }

    public MantisAbstractException(String message) {
        this(message, (MantisErrorMsg) null);
    }

    public MantisAbstractException(String message, MantisErrorMsg mantisErrorMsg) {
        super(message);
        this.mantisErrorMsg = mantisErrorMsg;
        this.errorMsg = message;
    }

    public MantisAbstractException(String message, MantisErrorMsg mantisErrorMsg, boolean writableStackTrace) {
        this(message, null, mantisErrorMsg, writableStackTrace);
    }

    public MantisAbstractException(String message, Throwable cause, MantisErrorMsg mantisErrorMsg, boolean writableStackTrace) {
        super(message, cause, false, writableStackTrace);
        this.mantisErrorMsg = mantisErrorMsg;
        this.errorMsg = message;
    }

    public MantisAbstractException(String message, Throwable cause) {
        this(message, cause, null);
    }

    public MantisAbstractException(String message, Throwable cause, MantisErrorMsg mantisErrorMsg) {
        super(message, cause);
        this.mantisErrorMsg = mantisErrorMsg;
        this.errorMsg = message;
    }

    public MantisAbstractException(Throwable cause) {
        super(cause);
    }

    public MantisAbstractException(Throwable cause, MantisErrorMsg mantisErrorMsg) {
        super(cause);
        this.mantisErrorMsg = mantisErrorMsg;
    }

    @Override
    public String getMessage() {
        String message = getOriginMessage();
        return "error_message: " + message + ", status: " + mantisErrorMsg.getStatus() + ", error_code: " + mantisErrorMsg.getErrorCode()
                + ",r=" + RpcContext.getContext().getRequestId();
    }

    public String getOriginMessage() {
        if (mantisErrorMsg == null) {
            return super.getMessage();
        }

        String message;

        if (errorMsg != null && !"".equals(errorMsg)) {
            message = errorMsg;
        } else {
            message = mantisErrorMsg.getMessage();
        }
        return message;
    }

    public int getStatus() {
        return mantisErrorMsg != null ? mantisErrorMsg.getStatus() : 0;
    }

    public int getErrorCode() {
        return mantisErrorMsg != null ? mantisErrorMsg.getErrorCode() : 0;
    }

    public MantisErrorMsg getmantisErrorMsg() {
        return mantisErrorMsg;
    }
}
