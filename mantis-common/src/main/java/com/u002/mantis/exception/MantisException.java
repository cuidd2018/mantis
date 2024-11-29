package com.u002.mantis.exception;

/**
 * 运行时异常定义
 */
public class MantisException extends RuntimeException {

    /**
     * 构造函数
     * @param msg 异常信息
     */
    public MantisException(String msg) {
        super(msg);
    }
}
