package com.u002.core.rpc;

import com.u002.core.common.URLParamType;

import java.util.HashMap;
import java.util.Map;

/**
 * rpc session context
 *
 * @author amber
 */
public class RpcContext {
    private static final ThreadLocal<RpcContext> LOCAL_CONTEXT = new ThreadLocal<RpcContext>() {
        @Override
        protected RpcContext initialValue() {return new RpcContext();
        }
    };
    private final Map<Object, Object> attributes = new HashMap<Object, Object>();
    private final Map<String, String> attachments = new HashMap<String, String>();// attachment in rpc context. not same with request's attachments
    private Request request;
    private Response response;
    private String clientRequestId = null;

    public static RpcContext getContext() {
        return LOCAL_CONTEXT.get();
    }

    /**
     * init new rpcContext with request
     *
     * @param request 请求对象
     * @return 上下文信息
     */
    public static RpcContext init(Request request) {
        RpcContext context = new RpcContext();
        if (request != null) {
            context.setRequest(request);
            context.setClientRequestId(request.getAttachments().get(URLParamType.requestIdFromClient.getName()));
        }
        LOCAL_CONTEXT.set(context);
        return context;
    }


    /**
     * 初始化
     * @return 上下文信息
     */
    public static RpcContext init() {
        RpcContext context = new RpcContext();
        LOCAL_CONTEXT.set(context);
        return context;
    }

    /**
     * 销毁
     */
    public static void destroy() {
        LOCAL_CONTEXT.remove();
    }

    /**
     * clientRequestId > request.id
     *
     * @return 生成请求ID
     */
    public String getRequestId() {
        if (clientRequestId != null) {
            return clientRequestId;
        } else {
            return request == null ? null : String.valueOf(request.getRequestId());
        }
    }

    public void putAttribute(Object key, Object value) {
        attributes.put(key, value);
    }

    public Object getAttribute(Object key) {
        return attributes.get(key);
    }

    public void removeAttribute(Object key) {
        attributes.remove(key);
    }

    public Map<Object, Object> getAttributes() {
        return attributes;
    }

    public void setRpcAttachment(String key, String value) {
        attachments.put(key, value);
    }

    /**
     * get attachments from rpccontext only. not from request or response
     *
     * @param key
     * @return
     */
    public String getRpcAttachment(String key) {
        return attachments.get(key);
    }

    public void removeRpcAttachment(String key) {
        attachments.remove(key);
    }

    public Map<String, String> getRpcAttachments() {
        return attachments;
    }

    public Request getRequest() {
        return request;
    }

    public void setRequest(Request request) {
        this.request = request;
    }

    public Response getResponse() {
        return response;
    }

    public void setResponse(Response response) {
        this.response = response;
    }

    public String getClientRequestId() {
        return clientRequestId;
    }

    public void setClientRequestId(String clientRequestId) {
        this.clientRequestId = clientRequestId;
    }

}
