package com.u002.core.exception;

/**
 * @author amber
 * @version 创建时间：2013-5-30
 */
public class MantisErrorMsgConstant {
    public static final String PROVIDER_NOT_EXIST_EXCEPTION_PREFIX = "provider not exist serviceKey=";

    // service error status 503
    public static final int SERVICE_DEFAULT_ERROR_CODE = 10001;
    public static final int SERVICE_REJECT_ERROR_CODE = 10002;
    public static final int SERVICE_TIMEOUT_ERROR_CODE = 10003;
    public static final int SERVICE_TASK_CANCEL_ERROR_CODE = 10004;
    // service error status 404
    public static final int SERVICE_UNFOUND_ERROR_CODE = 10101;
    // service error status 403
    public static final int SERVICE_REQUEST_LENGTH_OUT_OF_LIMIT_ERROR_CODE = 10201;
    // framework error
    public static final int FRAMEWORK_DEFAULT_ERROR_CODE = 20001;
    public static final int FRAMEWORK_ENCODE_ERROR_CODE = 20002;
    public static final int FRAMEWORK_DECODE_ERROR_CODE = 20003;
    public static final int FRAMEWORK_INIT_ERROR_CODE = 20004;
    public static final int FRAMEWORK_EXPORT_ERROR_CODE = 20005;
    public static final int FRAMEWORK_SERVER_ERROR_CODE = 20006;
    public static final int FRAMEWORK_REFER_ERROR_CODE = 20007;
    public static final int FRAMEWORK_REGISTER_ERROR_CODE = 20008;
    // biz exception
    public static final int BIZ_DEFAULT_ERROR_CODE = 30001;

    // service errors
    public static final MantisErrorMsg SERVICE_NOT_SUPPORT_ERROR = new MantisErrorMsg(501, 501, "service not support");
    public static final MantisErrorMsg SERVICE_DEFAULT_ERROR = new MantisErrorMsg(503, SERVICE_DEFAULT_ERROR_CODE, "service error");
    public static final MantisErrorMsg SERVICE_REJECT = new MantisErrorMsg(503, SERVICE_REJECT_ERROR_CODE, "service reject");
    public static final MantisErrorMsg SERVICE_UNFOUND = new MantisErrorMsg(404, SERVICE_UNFOUND_ERROR_CODE, "service unfound");
    // server end provider not exist exception. client end will force close connection if encounter this exception.
    public static final MantisErrorMsg PROVIDER_NOT_EXIST = new MantisErrorMsg(404, 404, PROVIDER_NOT_EXIST_EXCEPTION_PREFIX);
    public static final MantisErrorMsg SERVICE_TIMEOUT = new MantisErrorMsg(503, SERVICE_TIMEOUT_ERROR_CODE, "service request timeout");
    public static final MantisErrorMsg SERVICE_TASK_CANCEL = new MantisErrorMsg(503, SERVICE_TASK_CANCEL_ERROR_CODE, "service task cancel");
    public static final MantisErrorMsg SERVICE_REQUEST_LENGTH_OUT_OF_LIMIT = new MantisErrorMsg(403,
            SERVICE_REQUEST_LENGTH_OUT_OF_LIMIT_ERROR_CODE, "service request data length over of limit");

    // framework errors
    public static final MantisErrorMsg FRAMEWORK_DEFAULT_ERROR = new MantisErrorMsg(503, FRAMEWORK_DEFAULT_ERROR_CODE,
            "framework default error");
    public static final MantisErrorMsg FRAMEWORK_ENCODE_ERROR =
            new MantisErrorMsg(503, FRAMEWORK_ENCODE_ERROR_CODE, "framework encode error");
    public static final MantisErrorMsg FRAMEWORK_DECODE_ERROR =
            new MantisErrorMsg(503, FRAMEWORK_DECODE_ERROR_CODE, "framework decode error");
    public static final MantisErrorMsg FRAMEWORK_INIT_ERROR = new MantisErrorMsg(500, FRAMEWORK_INIT_ERROR_CODE, "framework init error");
    public static final MantisErrorMsg FRAMEWORK_EXPORT_ERROR =
            new MantisErrorMsg(503, FRAMEWORK_EXPORT_ERROR_CODE, "framework export error");
    public static final MantisErrorMsg FRAMEWORK_REFER_ERROR = new MantisErrorMsg(503, FRAMEWORK_REFER_ERROR_CODE, "framework refer error");

    // biz errors
    public static final MantisErrorMsg BIZ_DEFAULT_EXCEPTION = new MantisErrorMsg(503, BIZ_DEFAULT_ERROR_CODE, "provider error");

    private MantisErrorMsgConstant() {
    }
}
