package com.u002.core.exception;

import java.io.Serializable;

/**
 * @author amber
 * @version 创建时间：2013-5-30
 */
public class MantisErrorMsg implements Serializable {
    private static final long serialVersionUID = 4909459500370103048L;

    private int status;
    private int errorcode;
    private String message;

    public MantisErrorMsg() {
    }

    public MantisErrorMsg(int status, int errorcode, String message) {
        this.status = status;
        this.errorcode = errorcode;
        this.message = message;
    }

    public int getStatus() {
        return status;
    }

    public int getErrorCode() {
        return errorcode;
    }

    public String getMessage() {
        return message;
    }

}
