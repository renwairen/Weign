package com.github.renwairen.weign.domain;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.github.renwairen.weign.ABIBuilder;
import com.github.renwairen.weign.constant.InterfaceType;
import com.github.renwairen.weign.exception.FunctionNotFoundException;
import org.web3j.utils.Numeric;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Application Binary Interace
 *
 * <p>{@link ABIBuilder} @Author Zhang La @Created at 2022/7/6 11:09
 */
public class ABI {

    /**
     * function 的 methodId 长度， 8位值加上 0x标识
     */
    private static final int HEX_METHOD_ID_LENGTH = Function.SIGN_LENGTH + "0x".length();

    private final InterfaceContainer<Event> eventContainer;

    private final InterfaceContainer<Function> functionContainer;

    public ABI(List<InterfaceDescriptor> descriptors) {
        List<Event> events = new ArrayList<>();
        List<Function> functions = new ArrayList<>();
        for (InterfaceDescriptor descriptor : descriptors) {
            final InterfaceType type = InterfaceType.fromValue(descriptor.getType());
            if (InterfaceType.EVENT == type) {
                events.add(new Event(descriptor));
            } else if (InterfaceType.FUNCTION == type) {
                functions.add(new Function(descriptor));
            }
        }
        this.eventContainer = new InterfaceContainer<>(events);
        this.functionContainer = new InterfaceContainer<>(functions);
    }

    /**
     * 按照name获取最后一个function，当有同名方法，可能不准确
     *
     * @param name
     * @return
     */
    public Function getFunctionByName(String name) {
        return functionContainer.getByName(name);
    }

    /**
     * 按照name和参数数量获取最后一个function，当有同名方法，可能不准确
     *
     * @param name
     * @param argNo
     * @return
     */
    public Function getFunctionByName(String name, int argNo) {
        return functionContainer.getByName(name, argNo);
    }

    public Function getFunctionBySign(String sign) {
        return functionContainer.getBySign(sign);
    }

    /**
     * 按照name获取最后一个event，当有同名event，可能不准确
     *
     * @param name
     * @return
     */
    public Event getEventByName(String name) {
        return eventContainer.getByName(name);
    }

    public Event getEventBySign(String sign) {
        return eventContainer.getBySign(sign);
    }

    /**
     * @return topic -> event
     */
    public Map<String, Event> getEventMap() {
        return eventContainer.getSignMap();
    }

    /**
     * @return sign -> function
     */
    public Map<String, Function> getFunctionMap() {
        return functionContainer.getSignMap();
    }

    /**
     * 非匿名event消息解码
     *
     * @param topics topics[0] 为消息签名，用于消息选择
     * @param data
     * @return nullable
     */
    public EventData decodeEvent(String[] topics, String data) {
        if (topics == null || topics.length == 0) {
            return null;
        }
        Event event = getEventBySign(topics[0]);
        if (event == null) {
            return null;
        }

        ObjectNode topicsNode = event.decode(topics);
        ObjectNode dataNode = event.decode(data);
        return new EventData(event, topicsNode, dataNode);
    }

    /**
     * 非匿名event消息解码
     *
     * @param topics topics[0] 为消息签名，用于消息选择
     * @param data
     * @return nullable
     */
    public EventData decodeEvent(List<String> topics, String data) {
        if (topics == null || topics.isEmpty()) {
            return null;
        }
        return decodeEvent(topics.toArray(new String[0]), data);
    }

    /**
     * 方法参数编码
     *
     * @param name
     * @param args
     * @return
     * @throws FunctionNotFoundException
     */
    public String encodeFunctionAsString(String name, Object... args)
            throws FunctionNotFoundException {
        Function function = getFunctionByName(name);
        if (function == null) {
            throw new RuntimeException(name);
        }
        return function.encode(args);
    }

    /**
     * 方法参数编码
     *
     * @param name
     * @param args
     * @return
     * @throws FunctionNotFoundException
     */
    public byte[] encodeFunctionAsBytes(String name, Object... args)
            throws FunctionNotFoundException {
        String data = encodeFunctionAsString(name, args);
        return Numeric.hexStringToByteArray(data);
    }

    /**
     * 方法入参解码
     *
     * @param data 开头必须为0x；然后必须有8个字符的methodId；然后是data
     * @return
     * @throws FunctionNotFoundException
     */
    public FunctionData decodeFunction(String data) throws FunctionNotFoundException {
        if (data == null || data.isEmpty()) {
            throw new FunctionNotFoundException("Data cannot be empty");
        }
        if (data.length() < HEX_METHOD_ID_LENGTH) {
            throw new FunctionNotFoundException("MethodId not found");
        }

        String sign = data.substring(0, HEX_METHOD_ID_LENGTH);
        Function function = getFunctionBySign(sign);
        if (function == null) {
            throw new FunctionNotFoundException(sign);
        }
        // 解码的是入参数据
        data = data.substring(HEX_METHOD_ID_LENGTH);
        JsonNode jsonNode = Interface.decode(function.getInputs(), data);
        return new FunctionData(function, jsonNode);
    }

    /**
     * 方法入参解码
     *
     * @param data 开头必须为0x；然后必须有8个字符的methodId；然后是data
     * @return
     * @throws FunctionNotFoundException
     */
    public FunctionData decodeFunction(byte[] data) throws FunctionNotFoundException {
        if (data == null || data.length == 0) {
            throw new FunctionNotFoundException("Data cannot be empty");
        }
        return decodeFunction(Numeric.toHexString(data));
    }
}
