package com.github.renwairen.weign.codec.decoder;


import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.github.renwairen.weign.domain.Param;
import com.github.renwairen.weign.util.JsonUtil;

/**
 * 匿名事件，不包含事件签名 @Author Zhang La @Created at 2022/7/13 10:23
 */
public class AnonymousTopicDecoder extends TopicDecoder {

    public AnonymousTopicDecoder(Param[] indexeds) {
        super(indexeds);
        super.length -= 1;
    }

    @Override
    public ObjectNode decode(String[] topics) {
        super.validate(topics);
        ObjectNode objectNode = JsonUtil.objectNode();
        for (int i = 0; i < topics.length; ++i) {
            Param param = params[i];
            JsonNode jsonNode = super.decode(param, topics[i]);
            objectNode.set(param.getName(), jsonNode);
        }
        return objectNode;
    }
}
