package com.u002.serialize.protostuff;

import com.dyuproject.protostuff.LinkedBuffer;
import com.dyuproject.protostuff.ProtostuffIOUtil;
import com.dyuproject.protostuff.Schema;
import com.dyuproject.protostuff.runtime.RuntimeSchema;
import com.u002.basic.serialize.Serializer;
import org.objenesis.Objenesis;
import org.objenesis.ObjenesisStd;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author amber
 * @date 2017-08-09 16:01:52
 * @description 类功能说明
 */
public class ProtostuffSerialization implements Serializer {

    private static final Map<Class<?>, Schema<?>> cachedSchema = new ConcurrentHashMap<Class<?>, Schema<?>>();

    /**
     * Avoid re applying buffer space every time serialization
     */
    private static final LinkedBuffer BUFFER = LinkedBuffer.allocate(LinkedBuffer.DEFAULT_BUFFER_SIZE);

    private static final Objenesis objenesis = new ObjenesisStd(true);

    private static <T> Schema<T> getSchema(Class<T> cls) {
        Schema<T> schema = (Schema<T>) cachedSchema.get(cls);
        if (schema == null) {
            schema = RuntimeSchema.createFrom(cls);
            if (schema != null) {
                cachedSchema.put(cls, schema);
            }
        }
        return schema;
    }

    /**
     * 序列化（对象->字节数组）
     *
     * @param data
     * @param <T>
     * @return
     */

    @Override
    public <T> byte[] serialize(T data) throws IOException {
        Class<T> cls = (Class<T>) data.getClass();
        try {
            Schema<T> schema = getSchema(cls);
            return ProtostuffIOUtil.toByteArray(data, schema, BUFFER);
        } catch (Exception e) {
            throw new IllegalStateException(e.getMessage(), e);
        } finally {
           // BUFFER.clear();
        }
    }

    /**
     * 反序列化（字节数组->对象）
     *   Schema<T> schema = RuntimeSchema.getSchema(clazz);
     *   T obj = schema.newMessage();
     *   ProtostuffIOUtil.mergeFrom(bytes, obj, schema);
     *   return obj;
     * @param data
     * @param cls
     * @param <T>
     * @return
     */
    @Override
    public <T> T deserialize(byte[] data, Class<T> cls) {
        try {
            T message = objenesis.newInstance(cls);
            Schema<T> schema = getSchema(cls);
            ProtostuffIOUtil.mergeFrom(data, message, schema);
            return message;
        } catch (Exception e) {
            throw new IllegalStateException(e.getMessage(), e);
        }
    }
}