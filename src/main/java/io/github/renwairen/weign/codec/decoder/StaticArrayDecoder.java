package io.github.renwairen.weign.codec.decoder;


import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import io.github.renwairen.weign.domain.Param;
import io.github.renwairen.weign.domain.ParamMeta;
import io.github.renwairen.weign.util.JsonUtil;

/**
 * @Author Zhang La @Created at 2022/7/11 19:04
 */
public class StaticArrayDecoder implements Decoder {

    /**
     * 数组元素数量
     */
    private final int count;

    private final Param param;

    /**
     * 数组元素长度
     */
    private final int headLength;

    public StaticArrayDecoder(ParamMeta meta) {
        this.count = meta.length;
        this.param = meta.item;
        this.headLength = param.getHeadLength();
    }

    @Override
    public ArrayNode decode(String data, int offset) {
        ArrayNode arrayNode = JsonUtil.arrayNode();
        for (int i = 0; i < count; ++i) {
            JsonNode value = param.decode(data, offset);
            offset += headLength;
            arrayNode.add(value);
        }
        return arrayNode;
    }
}
