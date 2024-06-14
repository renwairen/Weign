package io.github.renwairen.weign.codec.decoder;


import com.fasterxml.jackson.databind.JsonNode;
import io.github.renwairen.weign.codec.DataTypes;
import io.github.renwairen.weign.codec.converter.Converter;

/**
 * bool, uint.... 静态基本类型的解码 @Author Zhang La @Created at 2022/7/12 10:48
 */
public class StaticTypeDecoder implements Decoder {

    private final Converter converter;

    private final int headLength;

    public StaticTypeDecoder(String type, int headLength) {
        this.converter = DataTypes.getConverter(type);
        this.headLength = headLength;
    }

    @Override
    public JsonNode decode(String data, int offset) {
        data = data.substring(offset, offset + headLength);
        return converter.decode(data);
    }
}
