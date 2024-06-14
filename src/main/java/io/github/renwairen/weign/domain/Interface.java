package io.github.renwairen.weign.domain;


import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import io.github.renwairen.weign.constant.ParamType;
import io.github.renwairen.weign.util.JsonUtil;
import lombok.Getter;
import org.web3j.crypto.Hash;
import org.web3j.utils.Numeric;

/**
 * @Author Zhang La @Created at 2022/7/6 14:23
 */
@Getter
public abstract class Interface {

    protected final InterfaceDescriptor descriptor;

    protected final String name;

    protected final Param[] inputs;

    protected final String sign;

    /**
     * @param descriptor
     * @param signLength 方法签名前缀长度，不包含前缀 0x 的长度
     */
    public Interface(InterfaceDescriptor descriptor, int signLength) {
        this.descriptor = descriptor;
        this.name = descriptor.getName();
        this.inputs = toParams(descriptor.getInputs());
        this.sign = genSign().substring(0, signLength + 2);
    }

    private String genSign() {
        String definition = name + getType(inputs);
        byte[] bytes = definition.getBytes();
        bytes = Hash.sha3(bytes);
        return Numeric.toHexString(bytes);
    }

    /**
     * @param params
     * @param data   可以以0x开头，也可以不以
     * @return
     */
    public static ObjectNode decode(Param[] params, String data) {
        ObjectNode objectNode = JsonUtil.objectNode();
        if (params == null || params.length == 0) {
            return objectNode;
        }
        data = Numeric.cleanHexPrefix(data);
        if (data == null || data.isEmpty()) {
            return objectNode;
        }

        int offset = 0;
        for (Param param : params) {
            JsonNode value = param.decode(data, offset);
            objectNode.set(param.getName(), value);
            offset += param.getHeadLength();
        }
        return objectNode;
    }

    private static String getType(Param[] params) {
        if (params == null || params.length == 0) {
            return "()";
        }
        StringBuilder builder = new StringBuilder();
        builder.append('(');
        builder.append(getType(params[0]));
        for (int i = 1; i < params.length; ++i) {
            builder.append(',').append(getType(params[i]));
        }
        builder.append(')');
        return builder.toString();
    }

    /**
     * tuple需要展开
     *
     * @param param
     * @return
     */
    private static String getType(Param param) {
        final String type = param.getType();
        int index = type.indexOf(ParamType.TUPLE.getValue());
        if (index > -1) {
            StringBuilder builder = new StringBuilder();
            builder.append(getType(param.getComponents()));
            // tuple[] -> (uint, bool)[]
            builder.append(type.substring(ParamType.TUPLE.getValue().length()));
            return builder.toString();
        }
        return type;
    }

    /**
     * @param descriptors nullable
     * @return
     */
    static Param[] toParams(ParamDescriptor[] descriptors) {
        if (descriptors == null) {
            return null;
        }
        final int length = descriptors.length;
        Param[] params = new Param[length];
        for (int i = 0; i < length; ++i) {
            params[i] = new Param(descriptors[i]);
        }
        return params;
    }
}
