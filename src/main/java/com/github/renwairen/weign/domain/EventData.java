package com.github.renwairen.weign.domain;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.github.renwairen.weign.util.JsonUtil;
import lombok.Data;

/**
 * @Author Zhang La @Created at 2022/7/13 12:45
 */
@Data
public class EventData {

    /**
     * 如果为null，则表示未查询到相应日志
     */
    private final Event event;

    private final ObjectNode topics;

    private final ObjectNode data;

    /**
     * 将topics和data融合到一个json
     *
     * @return
     */
    public JsonNode getAll() {
        ObjectNode objectNode = JsonUtil.objectNode();
        objectNode.setAll(topics);
        objectNode.setAll(data);
        return objectNode;
    }
}
