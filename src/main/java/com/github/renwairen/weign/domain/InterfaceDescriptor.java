package com.github.renwairen.weign.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

/**
 * 接口定义 @Author Zhang La @Created at 2022/6/21 11:26
 */
@Data
public class InterfaceDescriptor {

    private final Boolean constant;

    /**
     * constructor, function, receive, event
     */
    private final String type;

    private final String name;

    private final ParamDescriptor[] inputs;

    private final ParamDescriptor[] outputs;

    /**
     * nonpayable, view, payable, pure
     */
    private final String stateMutability;

    private final Boolean anonymous;

    public InterfaceDescriptor(
            @JsonProperty("constant") Boolean constant,
            @JsonProperty("type") String type,
            @JsonProperty("name") String name,
            @JsonProperty("inputs") ParamDescriptor[] inputs,
            @JsonProperty("outputs") ParamDescriptor[] outputs,
            @JsonProperty("stateMutability") String stateMutability,
            @JsonProperty("anonymous") Boolean anonymous) {
        this.constant = constant;
        this.type = type;
        this.name = name;
        this.inputs = inputs;
        this.outputs = outputs;
        this.stateMutability = stateMutability;
        this.anonymous = anonymous;
    }
}
