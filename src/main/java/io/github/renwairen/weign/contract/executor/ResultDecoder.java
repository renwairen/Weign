package io.github.renwairen.weign.contract.executor;


import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonNode;
import io.github.renwairen.weign.domain.Function;
import io.github.renwairen.weign.domain.Param;
import io.github.renwairen.weign.util.FunctionUtil;
import io.github.renwairen.weign.util.JsonUtil;
import lombok.AllArgsConstructor;

import java.lang.reflect.Method;

/**
 * @Author Zhang La @Created at 2023/1/11 14:15
 */
public class ResultDecoder<T> {

    private final Function function;

    private final JavaType type;

    private final Resolver resolver;

    public ResultDecoder(Function function, Method method) {
        this(function, FunctionUtil.getReturnType(method));
    }

    public ResultDecoder(Function function, Class<T> clazz) {
        this(function, JsonUtil.constructType(clazz));
    }

    public ResultDecoder(Function function, TypeReference<T> reference) {
        this(function, JsonUtil.constructType(reference));
    }

    private ResultDecoder(Function function, JavaType type) {
        this.function = function;
        this.resolver = genResolver(function);
        this.type = type;
    }

    public T decode(String data) {
        if (data == null) {
            return null;
        }
        JsonNode jsonNode = function.decode(data);
        return this.decode(jsonNode);
    }

    public T decode(JsonNode jsonNode) {
        if (jsonNode == null || jsonNode.isNull() || jsonNode.isEmpty() || type == null) {
            return null;
        }
        jsonNode = resolver.resolve(jsonNode);
        return JsonUtil.read(jsonNode.toString(), type);
    }

    private Resolver genResolver(Function function) {
        Param[] params = function.getOutputs();
        if (params.length == 1) {
            return new ValueResolver(params[0].getName());
        } else {
            return IdentityResolver.getInstance();
        }
    }

    /**
     * 用于结果映射 <br>
     * 比如单个返回结果，直接将返回内容作为返回值，不需要再封装为 bean
     */
    private interface Resolver {

        JsonNode resolve(JsonNode jsonNode);
    }

    private static class IdentityResolver implements Resolver {

        private static final IdentityResolver INSTANCE = new IdentityResolver();

        @Override
        public JsonNode resolve(JsonNode jsonNode) {
            return jsonNode;
        }

        public static IdentityResolver getInstance() {
            return INSTANCE;
        }
    }

    @AllArgsConstructor
    private static class ValueResolver implements Resolver {

        private final String name;

        @Override
        public JsonNode resolve(JsonNode jsonNode) {
            return jsonNode.get(name);
        }
    }
}
