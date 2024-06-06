package com.u002.mantis.api;


/**
 * <p>ByteBuf的一个抽象，这样可以隔离各种Bytebuf</p>
 *
 * @author amber
 */
public interface ByteBuf {

    /**
     * Get byte[] data
     *
     * @return byte[]
     */
    public byte[] array();

    /**
     * Get length of readable bytes
     *
     * @return length
     */
    public int readableBytes();

    /**
     * release byte buffer
     *
     * @return result
     */
    public boolean release();
}
