package io.github.renwairen.weign.codec.encoder;


import com.fasterxml.jackson.databind.JsonNode;
import io.github.renwairen.weign.codec.DataTypes;
import io.github.renwairen.weign.codec.converter.Converter;
import org.web3j.abi.TypeEncoder;
import org.web3j.abi.datatypes.Type;

/**
 * @Author Zhang La @Created at 2022/7/12 16:40
 */
public class BasicTypeEncoder implements Encoder {

    private final Converter converter;

    public BasicTypeEncoder(String type) {
        this.converter = DataTypes.getConverter(type);
    }

    @Override
    public String encode(JsonNode arg) {
        Type<?> type = converter.encode(arg);
        return TypeEncoder.encode(type);
    }
}
