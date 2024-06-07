package com.github.renwairen.weign.domain;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.NullNode;
import com.github.renwairen.weign.codec.decoder.*;
import com.github.renwairen.weign.codec.encoder.*;
import com.github.renwairen.weign.constant.ParamType;
import lombok.Getter;

/**
 * @Author Zhang La @Created at 2022/7/6 13:40
 */
@Getter
public class Param {

    private final String name;

    /**
     * tuple, uint256
     */
    private final String type;

    /**
     * 当类型为tuple类型时，该属性会有值
     */
    private final Param[] components;

    private final boolean dynamic;

    /**
     * 参数长度，对于动态类型，长度固定为64
     */
    private final int headLength;

    /**
     * value -> data
     */
    private final Encoder encoder;

    /**
     * data, offset -> value
     */
    private final Decoder decoder;

    public Param(ParamDescriptor descriptor) {
        this(
                descriptor.getName(), descriptor.getType(), Interface.toParams(descriptor.getComponents()));
    }

    public Param(String name, String type, Param[] components) {
        this.name = name;
        this.type = type;
        this.components = components;
        ParamMeta meta = genMeta();
        this.dynamic = !isStatic(meta);
        // 放在最后，一些变量需要初始化
        this.headLength = genHeadLength(meta);
        this.encoder = genEncoder(meta);
        this.decoder = genDecoder(meta);
    }

    public String encode(JsonNode arg) {
        return encoder.encode(arg);
    }

    public JsonNode decode(String data, int offset) {
        if (data.length() <= offset) {
            return NullNode.getInstance();
        }
        return decoder.decode(data, offset);
    }

    private Decoder genDecoder(ParamMeta meta) {
        Decoder decoder;
        if (meta.isArray) {
            // 数组，需要优先判定，比如tuple[]，首先要按照数组进行解析
            decoder = meta.length == null ? new DynamicArrayDecoder(meta) : new StaticArrayDecoder(meta);
        } else if (ParamType.TUPLE.getValue().equals(type)) {
            // tuple
            decoder = new TupleDecoder(components);
        } else if (ParamType.DYNAMIC_TYPES.contains(type)) {
            decoder = new DynamicTypeDecoder(type);
        } else {
            decoder = new StaticTypeDecoder(type, headLength);
        }

        if (dynamic) {
            return new DynamicDecoder(decoder);
        }
        return decoder;
    }

    private Encoder genEncoder(ParamMeta meta) {
        // 数组
        if (meta.isArray) {
            return meta.length == null ? new DynamicArrayEncoder(meta) : new StaticArrayEncoder(meta);
        }

        if (ParamType.TUPLE.getValue().equals(type)) {
            return new TupleEncoder(components);
        }

        return new BasicTypeEncoder(type);
    }

    /**
     * 对于基本类型，bytes, string为动态类型，其余为静态类型
     *
     * <p>对于 T[]，为动态
     *
     * <p>对于 T[n]，取决于T是否为动态
     *
     * <p>对于 Tuple，取决于 components 元素是否为动态
     *
     * @return
     */
    private boolean isStatic(ParamMeta meta) {
        // bytes string
        if (ParamType.DYNAMIC_TYPES.contains(type)) {
            return false;
        }
        // 数组
        if (meta.isArray) {
            // []
            if (meta.length == null) {
                return false;
            }
            // [12]
            return !meta.item.isDynamic();
        }
        // tuple
        if (ParamType.TUPLE.getValue().equals(type)) {
            if (components == null || components.length == 0) {
                return true;
            }
            for (Param component : components) {
                if (component.isDynamic()) {
                    return false;
                }
            }
            return true;
        }
        // uint bool fixed address
        return true;
    }

    /**
     * @return left: 是否是数组；middle，数组长度，动态数组为 null；right，数组元素
     */
    private ParamMeta genMeta() {
        int index = type.lastIndexOf('[');

        boolean isArray = false;
        Integer length = null;
        Param item = null;
        // [], [132]
        if (index > -1) {
            isArray = true;
            item = new Param(name, type.substring(0, index), components);
            // [2]
            if (index + 2 < type.length()) {
                length = Integer.parseInt(type.substring(index + 1, type.length() - 1));
            }
        }
        return new ParamMeta(isArray, length, item);
    }

    private int genHeadLength(ParamMeta meta) {
        if (!dynamic) {
            // 元素数量*元素长度
            if (meta.isArray) {
                return meta.length * meta.item.getHeadLength();
            }
            if (ParamType.TUPLE.getValue().equals(type)) {
                if (components == null || components.length == 0) {
                    return 0;
                }
                int length = 0;
                for (Param component : components) {
                    length += component.getHeadLength();
                }
                return length;
            }
        }
        return Decoder.HEX_STRING_LENGTH;
    }
}
