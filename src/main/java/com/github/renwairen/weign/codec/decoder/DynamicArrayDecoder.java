package com.github.renwairen.weign.codec.decoder;


import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.github.renwairen.weign.domain.Param;
import com.github.renwairen.weign.domain.ParamMeta;
import com.github.renwairen.weign.util.JsonUtil;

/**
 * @Author Zhang La @Created at 2022/7/11 19:04
 */
public class DynamicArrayDecoder implements Decoder {

    private final Param param;

    private final int headLength;

    public DynamicArrayDecoder(ParamMeta meta) {
        this.param = meta.item;
        this.headLength = param.getHeadLength();
    }

    @Override
    public ArrayNode decode(String data, int offset) {
        ArrayNode arrayNode = JsonUtil.arrayNode();
        int count = Decoder.toIntValue(data, offset);
        offset = offset + Decoder.HEX_STRING_LENGTH;
        data = data.substring(offset);
        for (int i = 0; i < count; ++i) {
            JsonNode value = param.decode(data, i * headLength);
            arrayNode.add(value);
        }
        return arrayNode;
    }
}
