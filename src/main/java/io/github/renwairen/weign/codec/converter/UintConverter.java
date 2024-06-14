package io.github.renwairen.weign.codec.converter;


import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.BigIntegerNode;
import io.github.renwairen.weign.codec.type.LooseUint;
import io.github.renwairen.weign.util.JsonUtil;
import lombok.AllArgsConstructor;
import org.web3j.abi.datatypes.Type;

import java.math.BigInteger;

/**
 * @Author Zhang La @Created at 2022/6/27 16:39
 */
@AllArgsConstructor
public class UintConverter implements Converter {

    private final int bitSize;

    @Override
    public Type<?> encode(JsonNode arg) {
        if (arg == null || arg.isNull()) {
            return new LooseUint(bitSize, BigInteger.ZERO);
        }
        return new LooseUint(bitSize, JsonUtil.toBigInteger(arg));
    }

    @Override
    public BigIntegerNode decode(String data) {
        BigInteger value = new BigInteger(data, 16);
        return BigIntegerNode.valueOf(value);
    }
}
