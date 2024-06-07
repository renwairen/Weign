package com.github.renwairen.weign.codec.encoder;


import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.github.renwairen.weign.domain.Param;
import com.github.renwairen.weign.domain.ParamMeta;
import org.web3j.abi.TypeEncoder;
import org.web3j.abi.datatypes.generated.Uint256;

import java.util.function.BiConsumer;

/**
 * @Author Zhang La @Created at 2022/7/12 15:50
 */
public class DynamicArrayEncoder implements Encoder {

    private final Param param;

    private final BiConsumer<EncoderContext, String> consumer;

    public DynamicArrayEncoder(ParamMeta meta) {
        this.param = meta.item;
        if (param.isDynamic()) {
            this.consumer = (context, data) -> context.nextDynamic(data);
        } else {
            this.consumer = (context, data) -> context.nextStatic(data);
        }
    }

    @Override
    public String encode(JsonNode arg) {
        // 数组元素为静态，直接平铺
        if (arg == null || arg.isNull()) {
            return TypeEncoder.encode(new Uint256(0));
        }

        ArrayNode values = (ArrayNode) arg;
        EncoderContext context = new EncoderContext();
        for (JsonNode value : values) {
            String data = param.encode(value);
            consumer.accept(context, data);
        }
        return TypeEncoder.encode(new Uint256(values.size())) + context;
    }
}
