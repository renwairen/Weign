package com.github.renwairen.weign.codec.converter;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.BinaryNode;
import lombok.AllArgsConstructor;
import org.web3j.abi.datatypes.DynamicBytes;
import org.web3j.abi.datatypes.Type;
import org.web3j.utils.Numeric;

import java.io.IOException;

/**
 * @Author Zhang La @Created at 2022/6/27 17:34
 */
@AllArgsConstructor
public class DynamicBytesConverter implements Converter {

    @Override
    public Type<?> encode(JsonNode arg) {
        if (arg == null || arg.isNull()) {
            return DynamicBytes.DEFAULT;
        }
        try {
            return new DynamicBytes(arg.binaryValue());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public BinaryNode decode(String data) {
        byte[] values = Numeric.hexStringToByteArray(data);
        return BinaryNode.valueOf(values);
    }
}
