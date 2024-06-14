package io.github.renwairen.weign.constant;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;
import java.util.Map;

/**
 * @Author Zhang La @Created at 2022/7/4 16:36
 */
@Slf4j
@AllArgsConstructor
public enum InterfaceType {
    CONSTRUCTOR("constructor"),

    FUNCTION("function"),

    RECEIVE("receive"),

    EVENT("event"),

    /**
     * 默认方法
     */
    FALLBACK("fallback"),

    /**
     * 异常类型
     */
    ERROR("error"),
    ;

    private final String value;

    private static final Map<String, InterfaceType> valueMap = new HashMap<>();

    static {
        for (InterfaceType type : InterfaceType.values()) {
            valueMap.put(type.value, type);
        }
    }

    public static InterfaceType fromValue(String value) {
        InterfaceType type = valueMap.get(value);
        if (type == null) {
            log.warn("Unkown type: {}", value);
        }
        return type;
    }
}
