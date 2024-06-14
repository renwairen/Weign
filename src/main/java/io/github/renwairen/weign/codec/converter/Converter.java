package io.github.renwairen.weign.codec.converter;

import com.fasterxml.jackson.databind.JsonNode;
import org.web3j.abi.datatypes.Type;

/**
 * @param <T> biginteger, byte[], string, boolean @Author Zhang La @Created at 2022/6/27 16:38
 */
public interface Converter {

    /**
     * 根据参数进行编码映射，biginteger -> uint, int 等
     *
     * @param arg nullable
     * @return
     */
    Type<?> encode(JsonNode arg);

    /**
     * 从链上返回值中解析数据
     *
     * @param data nonnull
     * @return
     */
    JsonNode decode(String data);
}
