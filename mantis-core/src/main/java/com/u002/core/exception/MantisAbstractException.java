package com.u002.core.exception;


import com.u002.core.rpc.RpcContext;

/**
 * @author maijunsheng
 * @version 创建时间：2013-5-30
 */
public abstract class MantisAbstractException extends RuntimeException {
    private static final long serialVersionUID = -8742311167276890503L;

    protected MantisErrorMsg motanErrorMsg = MantisErrorMsgConstant.FRAMEWORK_DEFAULT_ERROR;
    protected String errorMsg = null;

    public MantisAbstractException() {
        super();
    }

    public MantisAbstractException(MantisErrorMsg motanErrorMsg) {
        super();
        this.motanErrorMsg = motanErrorMsg;
    }

    public MantisAbstractException(String message) {
        this(message, (MantisErrorMsg) null);
    }

    public MantisAbstractException(String message, MantisErrorMsg motanErrorMsg) {
        super(message);
        this.motanErrorMsg = motanErrorMsg;
        this.errorMsg = message;
    }

    public MantisAbstractException(String message, MantisErrorMsg motanErrorMsg, boolean writableStackTrace) {
        this(message, null, motanErrorMsg, writableStackTrace);
    }

    public MantisAbstractException(String message, Throwable cause, MantisErrorMsg motanErrorMsg, boolean writableStackTrace) {
        super(message, cause, false, writableStackTrace);
        this.motanErrorMsg = motanErrorMsg;
        this.errorMsg = message;
    }

    public MantisAbstractException(String message, Throwable cause) {
        this(message, cause, null);
    }

    public MantisAbstractException(String message, Throwable cause, MantisErrorMsg motanErrorMsg) {
        super(message, cause);
        this.motanErrorMsg = motanErrorMsg;
        this.errorMsg = message;
    }

    public MantisAbstractException(Throwable cause) {
        super(cause);
    }

    public MantisAbstractException(Throwable cause, MantisErrorMsg motanErrorMsg) {
        super(cause);
        this.motanErrorMsg = motanErrorMsg;
    }

    @Override
    public String getMessage() {
        String message = getOriginMessage();
        return "error_message: " + message + ", status: " + motanErrorMsg.getStatus() + ", error_code: " + motanErrorMsg.getErrorCode()
                + ",r=" + RpcContext.getContext().getRequestId();
    }

    public String getOriginMessage() {
        if (motanErrorMsg == null) {
            return super.getMessage();
        }

        String message;

        if (errorMsg != null && !"".equals(errorMsg)) {
            message = errorMsg;
        } else {
            message = motanErrorMsg.getMessage();
        }
        return message;
    }

    public int getStatus() {
        return motanErrorMsg != null ? motanErrorMsg.getStatus() : 0;
    }

    public int getErrorCode() {
        return motanErrorMsg != null ? motanErrorMsg.getErrorCode() : 0;
    }

    public MantisErrorMsg getMotanErrorMsg() {
        return motanErrorMsg;
    }
}
