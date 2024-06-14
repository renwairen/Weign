package io.github.renwairen.weign.domain;


import com.fasterxml.jackson.databind.JsonNode;
import io.github.renwairen.weign.codec.encoder.EncoderContext;
import io.github.renwairen.weign.constant.StateMutability;
import io.github.renwairen.weign.util.ArrayUtil;
import io.github.renwairen.weign.util.JsonUtil;
import lombok.Getter;

import java.util.function.BiConsumer;

/**
 * @Author Zhang La @Created at 2022/7/6 10:48
 */
@Getter
public class Function extends Interface {

    public static final int SIGN_LENGTH = 8;

    protected final Boolean constant;

    protected final Param[] outputs;

    protected final StateMutability stateMutability;

    protected final BiConsumer<EncoderContext, String>[] consumers;

    public Function(InterfaceDescriptor descriptor) {
        super(descriptor, SIGN_LENGTH);
        this.constant = descriptor.getConstant();
        this.outputs = toParams(descriptor.getOutputs());
        this.stateMutability = StateMutability.fromValue(descriptor.getStateMutability());
        this.consumers = new BiConsumer[inputs.length];
        for (int i = 0; i < inputs.length; ++i) {
            Param param = inputs[i];
            BiConsumer<EncoderContext, String> consumer;
            if (param.isDynamic()) {
                consumer = (context, data) -> context.nextDynamic(data);
            } else {
                consumer = (context, data) -> context.nextStatic(data);
            }
            consumers[i] = consumer;
        }
    }

    /**
     * methodId + data，0x开头
     *
     * <p>[], [n] -> list
     *
     * <p>tuple -> bean / list / map
     *
     * <p>address, string -> string
     *
     * <p>uint int fixed unfixed -> biginteger
     *
     * <p>bytes -> byte[]
     *
     * @param args
     * @return 0xabcdef0000000000000
     */
    public String encode(Object... args) {
        if (!ArrayUtil.isSameLength(inputs, args)) {
            throw new RuntimeException("Invalid argument count: " + name);
        }
        if (inputs == null || inputs.length == 0) {
            return super.sign;
        }
        EncoderContext context = new EncoderContext();
        for (int i = 0; i < inputs.length; ++i) {
            Param param = inputs[i];
            Object arg = args[i];
            JsonNode jsonNode = JsonUtil.toTree(arg);
            String value = param.encode(jsonNode);
            consumers[i].accept(context, value);
        }
        return super.sign + context;
    }

    /**
     * 对输出进行解码，
     *
     * @param data
     * @return
     */
    public JsonNode decode(String data) {
        return decode(outputs, data);
    }
}
