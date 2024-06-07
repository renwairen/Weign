package com.github.renwairen.weign.codec.converter;


import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.BigIntegerNode;
import com.github.renwairen.weign.codec.type.LooseFixed;
import com.github.renwairen.weign.util.JsonUtil;
import lombok.AllArgsConstructor;
import org.web3j.abi.datatypes.Type;
import org.web3j.utils.Numeric;

import java.math.BigInteger;

/**
 * @Author Zhang La @Created at 2022/6/27 16:39
 */
@AllArgsConstructor
public class FixedConverter implements Converter {

    private final int mBitSize;

    private final int nBitSize;

    @Override
    public Type<?> encode(JsonNode arg) {
        if (arg == null || arg.isNull()) {
            return new LooseFixed(mBitSize, nBitSize, BigInteger.ZERO);
        }
        return new LooseFixed(mBitSize, nBitSize, JsonUtil.toBigInteger(arg));
    }

    @Override
    public BigIntegerNode decode(String data) {
        byte[] inputByteArray = Numeric.hexStringToByteArray(data);
        int typeLengthAsBytes = (mBitSize + nBitSize) >> 3;

        byte[] resultByteArray = new byte[typeLengthAsBytes + 1];
        resultByteArray[0] = inputByteArray[0]; // take MSB as sign bit

        int valueOffset = Type.MAX_BYTE_LENGTH - typeLengthAsBytes;
        System.arraycopy(inputByteArray, valueOffset, resultByteArray, 1, typeLengthAsBytes);

        BigInteger value = new BigInteger(resultByteArray);
        return BigIntegerNode.valueOf(value);
    }
}
