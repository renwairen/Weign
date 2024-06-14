package io.github.renwairen.weign.codec.decoder;


import com.fasterxml.jackson.databind.JsonNode;
import io.github.renwairen.weign.codec.DataTypes;
import io.github.renwairen.weign.codec.converter.Converter;

/**
 * bytes, string , 动态基本类型的解码，第一个数据行为数据长度 @Author Zhang La @Created at 2022/7/12 10:48
 */
public class DynamicTypeDecoder implements Decoder {

    private final Converter converter;

    public DynamicTypeDecoder(String type) {
        this.converter = DataTypes.getConverter(type);
    }

    @Override
    public JsonNode decode(String data, int offset) {
        int length = Decoder.toIntValue(data, offset);
        data =
                data.substring(
                        offset + HEX_STRING_LENGTH, offset + HEX_STRING_LENGTH + length * 2);
        return converter.decode(data);
    }
}
