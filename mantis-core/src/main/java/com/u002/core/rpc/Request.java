package com.u002.core.rpc;

import java.util.Map;

/**
 * Request
 * 请求接口类
 *
 * @author fishermen
 * @version V1.0 created at: 2013-5-16
 */
public interface Request {

    /**
     * service interface
     */
    String getInterfaceName();

    /**
     * service method name
     */
    String getMethodName();

    /**
     * service method param desc (sign)
     */
    String getParamtersDesc();

    /**
     * service method param
     */
    Object[] getArguments();

    /**
     * get framework param
     */
    Map<String, String> getAttachments();

    /**
     * set framework param
     */
    void setAttachment(String name, String value);

    /**
     * request id
     */
    long getRequestId();

    /**
     * retries
     */
    int getRetries();

    /**
     * set retries
     */
    void setRetries(int retries);

    /**
     * set rpc protocol version. for compatible different version.
     * this value must set by server end while decode finish.
     * it only used in local, will not send to remote.
     *
     * @param rpcProtocolVersion protocol version. see @RpcProtocolVersion
     */
    void setRpcProtocolVersion(byte rpcProtocolVersion);

    byte getRpcProtocolVersion();

    /**
     * set the serialization number.
     * same to the protocol version, this value only used in server end for compatible.
     *
     * @param number
     */
    void setSerializeNumber(int number);

    int getSerializeNumber();

    /**
     * @param key attachment key
     * @return attachment value or null if key does not exist
     * @since 1.2.3
     */
    default String getAttachment(String key) {
        Map<String, String> attachments = getAttachments();
        if (attachments != null) {
            return attachments.get(key);
        }
        return null;
    }
}
