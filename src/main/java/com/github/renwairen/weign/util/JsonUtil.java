package com.github.renwairen.weign.util;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.type.TypeFactory;
import com.github.renwairen.weign.exception.JsonReadException;

import java.io.IOException;
import java.math.BigInteger;

/**
 * @Author Zhang La @Created at 2022/6/14 17:33
 */
public abstract class JsonUtil {

    private static final ObjectMapper OBJECT_MAPPER;

    static {
        // 16进制数据转换
//        DeserializationProblemHandler problemHandler =
//                new DeserializationProblemHandler() {
//                    @Override
//                    public Object handleWeirdStringValue(
//                            DeserializationContext ctxt,
//                            Class<?> targetType,
//                            String valueToConvert,
//                            String failureMsg)
//                            throws IOException {
//                        if (StringUtil.startsWith(valueToConvert, "0x")) {
//                            valueToConvert = valueToConvert.substring(2);
//                            BigInteger value = new BigInteger(valueToConvert, 16);
//                            if (BigInteger.class == targetType) {
//                                return value;
//                            } else if (Long.class == targetType) {
//                                return Long.valueOf(value.longValue());
//                            } else if (Integer.class == targetType) {
//                                return Integer.valueOf(value.intValue());
//                            }
//                        }
//                        return super.handleWeirdStringValue(ctxt, targetType, valueToConvert, failureMsg);
//                    }
//                };
        OBJECT_MAPPER =
                JsonMapper.builder()
                        .serializationInclusion(JsonInclude.Include.NON_NULL)
                        .configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false)
                        .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
                        .configure(JsonGenerator.Feature.WRITE_BIGDECIMAL_AS_PLAIN, true)
                        //.addHandler(problemHandler)
                        .build();
    }

    public static JsonNode nullNode() {
        return OBJECT_MAPPER.nullNode();
    }

    public static TypeFactory typeFactory() {
        return OBJECT_MAPPER.getTypeFactory();
    }

    public static <T> JavaType constructType(Class<T> clazz) {
        return OBJECT_MAPPER.constructType(clazz);
    }

    public static <T> JavaType constructType(TypeReference<T> reference) {
        return OBJECT_MAPPER.constructType(reference);
    }


    public static ObjectNode objectNode() {
        return OBJECT_MAPPER.createObjectNode();
    }

    public static ArrayNode arrayNode() {
        return OBJECT_MAPPER.createArrayNode();
    }


    public static <T> T read(String json, TypeReference<T> type) throws JsonReadException {
        if (json == null || json.isEmpty()) {
            return null;
        }
        try {
            return OBJECT_MAPPER.readValue(json, type);
        } catch (JsonProcessingException e) {
            throw new JsonReadException(e);
        }
    }

    public static <T> T read(String json, JavaType type) throws JsonReadException {
        if (json == null || json.isEmpty()) {
            return null;
        }
        try {
            return OBJECT_MAPPER.readValue(json, type);
        } catch (JsonProcessingException e) {
            throw new JsonReadException(e);
        }
    }

    public static <T> T read(JsonNode jsonNode, TypeReference<T> reference) throws JsonReadException {
        JsonParser jsonParser = OBJECT_MAPPER.treeAsTokens(jsonNode);
        try {
            return OBJECT_MAPPER.readValue(jsonParser, reference);
        } catch (IOException e) {
            throw new JsonReadException(e);
        } finally {
            IOUtil.close(jsonParser);
        }
    }

    public static <T> T read(JsonNode jsonNode, Class<T> clazz) throws JsonReadException {
        JsonParser jsonParser = OBJECT_MAPPER.treeAsTokens(jsonNode);
        try {
            return OBJECT_MAPPER.readValue(jsonParser, clazz);
        } catch (IOException e) {
            throw new JsonReadException(e);
        } finally {
            IOUtil.close(jsonParser);
        }
    }


    public static boolean isEmpty(JsonNode jsonNode) {
        return jsonNode == null || jsonNode.isNull() || jsonNode.isEmpty();
    }

    public static JsonNode toTree(Object value) {
        JsonNode jsonNode;
        if (value instanceof JsonNode) {
            jsonNode = (JsonNode) value;
        } else {
            jsonNode = JsonUtil.OBJECT_MAPPER.valueToTree(value);
        }
        return jsonNode;
    }

    /**
     * 很多情况下， biginteger 被序列化为字符串，需要特殊处理
     *
     * @param jsonNode
     * @return
     */
    public static BigInteger toBigInteger(JsonNode jsonNode) {
        if (jsonNode == null || jsonNode.isNull()) {
            return null;
        }
        if (jsonNode.isTextual()) {
            String value = jsonNode.textValue();
            if (value == null || value.isEmpty()) {
                return null;
            }
            return new BigInteger(value);
        }
        return jsonNode.bigIntegerValue();
    }
}
