package com.u002.mantis.api;

import com.u002.mantis.exception.MantisException;

import java.util.Map;

public interface Serializer {

    /**
     * 序列化
     *
     * @param object  对象
     * @param context 上下文
     * @return 序列化后的对象
     * @throws MantisException 序列化异常
     */
    ByteBuf encode(Object object, Map<String, String> context) throws MantisException;


    /**
     * 反序列化，只有类型，返回对象
     *
     * @param data    原始字节数组
     * @param clazz   期望的类型
     * @param context 上下文
     * @return 反序列化后的对象
     * @throws MantisException 序列化异常
     */
    Object decode(ByteBuf data, Class clazz, Map<String, String> context) throws MantisException;


}
