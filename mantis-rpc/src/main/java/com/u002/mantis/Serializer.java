package com.u002.mantis;


import java.io.IOException;


/**
 * @author			amber
 * @date 			2017-08-08 14:25:43
 * @description 	序列化扩展点
 */
public interface Serializer {

	/**
	 * 序列化
	 * @param data 对象
	 * @throws IOException io异常
	 */
	<T> byte[] serialize(T data)throws IOException;

	/**
	 *
	 * @param type clazz
	 * @return 反序列haul的对象
	 * @throws IOException io异常
	 */
    <T> T deserialize(byte[] bytes, Class<T> type)throws IOException;
	
}