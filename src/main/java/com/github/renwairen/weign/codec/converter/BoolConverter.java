package com.github.renwairen.weign.codec.converter;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.BooleanNode;
import org.web3j.abi.datatypes.Bool;
import org.web3j.abi.datatypes.Type;

/**
 * @Author Zhang La @Created at 2022/6/27 16:39
 */
public class BoolConverter implements Converter {

    @Override
    public Type<?> encode(JsonNode arg) {
        if (arg == null || arg.isNull()) {
            return Bool.DEFAULT;
        }
        return new Bool(arg.asBoolean());
    }

    @Override
    public BooleanNode decode(String data) {
        boolean value = data.contains("1");
        return BooleanNode.valueOf(value);
    }
}
