package com.github.renwairen.weign.domain;


import com.fasterxml.jackson.databind.node.ObjectNode;
import com.github.renwairen.weign.codec.decoder.AnonymousTopicDecoder;
import com.github.renwairen.weign.codec.decoder.TopicDecoder;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author Zhang La @Created at 2022/7/6 10:48
 */
@Getter
public class Event extends Interface {

    /**
     * 匿名，默认为false
     *
     * <p>true : 则事件签名不会参与索引，可以有4个参数被设置为 indexed
     *
     * <p>false: 事件签名会记录到topic[0]，可以有3个参数设置为indexed
     */
    private final boolean anonymous;

    private final Param[] datas;

    private final TopicDecoder decoder;

    public Event(InterfaceDescriptor descriptor) {
        super(descriptor, 64);
        this.anonymous = descriptor.getAnonymous();
        List<Param> topics = new ArrayList<>();
        List<Param> datas = new ArrayList<>();
        for (ParamDescriptor input : descriptor.getInputs()) {
            Param param = new Param(input);
            Boolean indexed = input.getIndexed();
            if (indexed == null || !indexed) {
                datas.add(param);
            } else {
                topics.add(param);
            }
        }
        this.datas = datas.toArray(new Param[0]);

        Param[] indexeds = topics.toArray(new Param[0]);
        if (anonymous) {
            this.decoder = new AnonymousTopicDecoder(indexeds);
        } else {
            this.decoder = new TopicDecoder(indexeds);
        }
    }

    /**
     * data部分
     *
     * @param data
     * @return
     */
    public ObjectNode decode(String data) {
        return Interface.decode(datas, data);
    }

    /**
     * topic部分
     *
     * @param topics
     * @return methodId -> indexed param，匿名事件，key为null
     */
    public ObjectNode decode(String[] topics) {
        return decoder.decode(topics);
    }
}
