package com.github.renwairen.weign.codec.converter;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.TextNode;
import org.web3j.abi.datatypes.Address;
import org.web3j.abi.datatypes.Type;

/**
 * @Author Zhang La @Created at 2022/6/27 16:39
 */
public class AddressConverter implements Converter {

    @Override
    public Type<?> encode(JsonNode arg) {
        if (arg == null || arg.isNull()) {
            return Address.DEFAULT;
        }
        return new Address(arg.asText());
    }

    @Override
    public TextNode decode(String data) {
        Address address = new Address(data);
        return TextNode.valueOf(address.getValue());
    }
}
