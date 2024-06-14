package io.github.renwairen.weign.domain;

import com.fasterxml.jackson.databind.JsonNode;
import lombok.Data;

/**
 * @Author Zhang La @Created at 2022/7/13 12:45
 */
@Data
public class FunctionData {

    /**
     * 如果为null，则表示未查询到相应日志
     */
    private final Function function;

    private final JsonNode data;
}
