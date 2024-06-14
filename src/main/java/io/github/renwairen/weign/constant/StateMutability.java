package io.github.renwairen.weign.constant;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;
import java.util.Map;

/**
 * @Author Zhang La @Created at 2022/7/6 14:43
 */
@Slf4j
@AllArgsConstructor
public enum StateMutability {

    /**
     * 会改变区块链状态，不能接收以太坊
     */
    NON_PAYABLE("nonpayable"),

    /**
     * 只查看
     */
    VIEW("view"),

    /**
     * 可以接收以太坊
     */
    PAYABLE("payable"),

    /**
     * 不读取区块链状态，不写入数据
     */
    PURE("pure"),
    ;

    private final String value;

    private static final Map<String, StateMutability> valueMap = new HashMap<>();

    static {
        for (StateMutability mutability : StateMutability.values()) {
            valueMap.put(mutability.value, mutability);
        }
    }

    public static StateMutability fromValue(String value) {
        StateMutability mutability = valueMap.get(value);
        if (mutability == null) {
            log.warn("Invalid stateMutability: {}", value);
        }
        return mutability;
    }
}
