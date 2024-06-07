package com.github.renwairen.weign.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

/**
 * 参数描述 @Author Zhang La @Created at 2022/6/21 11:32
 */
@Data
public class ParamDescriptor {

    private final String internalType;

    private final String name;

    /**
     * tuple, uint256
     */
    private final String type;

    private final Boolean indexed;

    /**
     * 当类型为tuple类型时，该属性会有值
     */
    private final ParamDescriptor[] components;

    public ParamDescriptor(
            @JsonProperty("internalType") String internalType,
            @JsonProperty("name") String name,
            @JsonProperty("type") String type,
            @JsonProperty("indexed") Boolean indexed,
            @JsonProperty("components") ParamDescriptor[] components) {
        this.internalType = internalType;
        this.name = name;
        this.type = type;
        this.indexed = indexed;
        this.components = components;
    }
}
