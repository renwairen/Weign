package io.github.renwairen.weign.codec;

import io.github.renwairen.weign.codec.converter.*;

import java.util.HashMap;
import java.util.Map;

/**
 * @Author Zhang La @Created at 2022/6/22 14:06
 */
public abstract class DataTypes {

    /**
     * sol参数类型-> java 类型转换器
     */
    private static final Map<String, Converter> CONVERTER_MAP = new HashMap<>();

    static {
        // uint8, uint16, ... uint256
        // int8, int16, ... int256
        for (int i = 8; i <= 256; i += 8) {
            CONVERTER_MAP.put("uint" + i, new UintConverter(i));
            CONVERTER_MAP.put("int" + i, new IntConverter(i));
        }

        // address
        CONVERTER_MAP.put("address", new AddressConverter());

        // bool
        CONVERTER_MAP.put("bool", new BoolConverter());

        // fixed, unfixed
        for (int i = 8; i <= 256; i += 8) {
            for (int j = 1; j <= 80; ++j) {
                CONVERTER_MAP.put("fixed" + i + "x" + j, new FixedConverter(i, j));
                CONVERTER_MAP.put("ufixed" + i + "x" + j, new UfixedConverter(i, j));
            }
        }

        // bytes1, bytes2, ... bytes32
        for (int i = 1; i <= 32; ++i) {
            CONVERTER_MAP.put("bytes" + i, new BytesConverter(i));
        }

        // bytes
        CONVERTER_MAP.put("bytes", new DynamicBytesConverter());

        // string
        CONVERTER_MAP.put("string", new StringConverter());

        // function
        CONVERTER_MAP.put("function", new AddressConverter());
    }

    public static Converter getConverter(String type) {
        Converter converter = CONVERTER_MAP.get(type);
        if (converter == null) {
            throw new RuntimeException("Invalid type: " + type);
        }
        return converter;
    }
}
