package io.github.renwairen.weign.codec.decoder;

import com.fasterxml.jackson.databind.JsonNode;
import lombok.AllArgsConstructor;
import org.web3j.utils.Numeric;

/**
 * 动态类型的解码
 *
 * <p>从第一块数据获取偏移量，再从偏移量处开始截取数据，进行后续解码 @Author Zhang La @Created at 2022/7/11 19:41
 */
@AllArgsConstructor
public class DynamicDecoder implements Decoder {

    private final Decoder decoder;

    @Override
    public JsonNode decode(String data, int offset) {
        String offsetStr = data.substring(offset, offset + HEX_STRING_LENGTH);
        int offsetVal = Numeric.toBigInt(offsetStr).intValue();
        data = data.substring(offsetVal * 2);
        return decoder.decode(data, 0);
    }
}
