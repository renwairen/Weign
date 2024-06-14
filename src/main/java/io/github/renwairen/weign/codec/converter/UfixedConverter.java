package io.github.renwairen.weign.codec.converter;


import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.BigIntegerNode;
import io.github.renwairen.weign.codec.type.LooseUfixed;
import io.github.renwairen.weign.util.JsonUtil;
import lombok.AllArgsConstructor;
import org.web3j.abi.datatypes.Type;

import java.math.BigInteger;

/**
 * @Author Zhang La @Created at 2022/6/27 16:39
 */
@AllArgsConstructor
public class UfixedConverter implements Converter {

    private final int mBitSize;

    private final int nBitSize;

    @Override
    public Type<?> encode(JsonNode arg) {
        if (arg == null || arg.isNull()) {
            return new LooseUfixed(mBitSize, nBitSize, BigInteger.ZERO);
        }
        return new LooseUfixed(mBitSize, nBitSize, JsonUtil.toBigInteger(arg));
    }

    @Override
    public BigIntegerNode decode(String data) {
        BigInteger value = new BigInteger(data, 16);
        return BigIntegerNode.valueOf(value);
    }
}
