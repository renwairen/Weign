package com.github.renwairen.weign.codec.decoder;

import com.fasterxml.jackson.databind.JsonNode;
import org.web3j.abi.datatypes.Type;
import org.web3j.utils.Numeric;

/**
 * @Author Zhang La @Created at 2022/7/11 19:27
 */
public interface Decoder {

    /**
     * 编码后单行字符串数据长度 64
     */
    int HEX_STRING_LENGTH = Type.MAX_BYTE_LENGTH << 1;

    JsonNode decode(String data, int offset);

    /**
     * 从数据中取64位转换为int
     *
     * @param data
     * @param offset
     * @return
     */
    static int toIntValue(String data, int offset) {
        String str = data.substring(offset, offset + HEX_STRING_LENGTH);
        return Numeric.toBigInt(str).intValue();
    }
}
