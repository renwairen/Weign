package io.github.renwairen.weign.codec.converter;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.TextNode;
import org.web3j.abi.datatypes.Type;
import org.web3j.abi.datatypes.Utf8String;
import org.web3j.utils.Numeric;

/**
 * @Author Zhang La @Created at 2022/6/27 17:40
 */
public class StringConverter implements Converter {

    @Override
    public Type<?> encode(JsonNode arg) {
        if (arg == null || arg.isNull()) {
            return Utf8String.DEFAULT;
        }
        return new Utf8String(arg.asText());
    }

    @Override
    public TextNode decode(String data) {
        byte[] bytes = Numeric.hexStringToByteArray(data);
        String value = new String(bytes);
        return TextNode.valueOf(value);
    }
}
