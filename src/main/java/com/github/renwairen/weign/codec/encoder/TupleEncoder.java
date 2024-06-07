package com.github.renwairen.weign.codec.encoder;

import com.fasterxml.jackson.databind.JsonNode;
import com.github.renwairen.weign.domain.Param;

import java.util.function.BiConsumer;

/**
 * @Author Zhang La @Created at 2022/7/12 15:50
 */
public class TupleEncoder implements Encoder {

    private static final BiConsumer<EncoderContext, String> STATIC_CONSUMER =
            (context, data) -> context.nextStatic(data);

    private static final BiConsumer<EncoderContext, String> DYNAMIC_CONSUMER =
            (context, data) -> context.nextDynamic(data);

    private final Param[] components;

    private final int length;

    private final BiConsumer<EncoderContext, String>[] consumers;

    public TupleEncoder(Param[] components) {
        this.components = components;
        this.length = components.length;
        this.consumers = new BiConsumer[components.length];
        for (int i = 0; i < components.length; ++i) {
            Param component = components[i];
            BiConsumer<EncoderContext, String> consumer;
            if (component.isDynamic()) {
                consumer = DYNAMIC_CONSUMER;
            } else {
                consumer = STATIC_CONSUMER;
            }
            consumers[i] = consumer;
        }
    }

    @Override
    public String encode(JsonNode arg) {
        // 自定义bean
        EncoderContext encoder = new EncoderContext();
        // 参数类型为tuple，但入参用的list
        if (arg.isArray()) {
            for (int i = 0; i < length; ++i) {
                Param component = components[i];
                String value = component.encode(arg.get(i));
                consumers[i].accept(encoder, value);
            }
        } else {
            for (int i = 0; i < length; ++i) {
                Param component = components[i];
                String value = component.encode(arg.get(component.getName()));
                consumers[i].accept(encoder, value);
            }
        }
        return encoder.toString();
    }
}
