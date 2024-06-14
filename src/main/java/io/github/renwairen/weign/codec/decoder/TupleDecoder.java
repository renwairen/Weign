package io.github.renwairen.weign.codec.decoder;


import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import io.github.renwairen.weign.domain.Param;
import io.github.renwairen.weign.util.JsonUtil;

/**
 * @Author Zhang La @Created at 2022/7/11 19:04
 */
public class TupleDecoder implements Decoder {

    private final Param[] components;

    public TupleDecoder(Param[] components) {
        this.components = components;
    }

    @Override
    public JsonNode decode(String data, int offset) {
        ObjectNode objectNode = JsonUtil.objectNode();
        for (Param component : components) {
            JsonNode value = component.decode(data, offset);
            offset += component.getHeadLength();
            objectNode.set(component.getName(), value);
        }
        return objectNode;
    }
}
