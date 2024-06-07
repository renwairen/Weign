package com.github.renwairen.weign.util;


import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.type.TypeFactory;
import com.github.renwairen.weign.contract.executor.ExecuteOption;
import com.github.renwairen.weign.domain.ABI;
import com.github.renwairen.weign.domain.Function;

import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

/**
 * @Author Zhang La @Created at 2023/1/12 10:50
 */
public abstract class FunctionUtil {

    /**
     * 方法是否是运行时确定上下文的类型
     *
     * @param method
     * @return
     */
    public static boolean isRunTimeOption(Method method) {
        Class<?>[] types = method.getParameterTypes();
        if (types == null || types.length < 1) {
            return false;
        }
        return types[0] == ExecuteOption.class;
    }

    /**
     * 获取返回类型，泛型需要特殊处理
     *
     * @param method
     * @return
     */
    public static JavaType getReturnType(Method method) {
        Class<?> returnType = method.getReturnType();
        Type genericReturnType = method.getGenericReturnType();

        Type type = returnType;
        // 泛型类型
        final TypeFactory typeFactory = JsonUtil.typeFactory();
        if (genericReturnType instanceof ParameterizedType) {
            ParameterizedType parameterizedType = (ParameterizedType) genericReturnType;
            Type[] actualTypes = parameterizedType.getActualTypeArguments();
            JavaType[] javaTypes = new JavaType[actualTypes.length];
            for (int i = 0; i < actualTypes.length; ++i) {
                javaTypes[i] = typeFactory.constructType(actualTypes[i]);
            }
            type = typeFactory.constructParametricType(returnType, javaTypes);
        }
        return typeFactory.constructType(type);
    }

    public static Function getFunction(ABI abi, Method method) {
        int paramCount = method.getParameterCount();
        if (isRunTimeOption(method)) {
            --paramCount;
        }
        com.github.renwairen.weign.annotation.Function annotation =
                method.getAnnotation(com.github.renwairen.weign.annotation.Function.class);
        Function function = abi.getFunctionByName(method.getName(), paramCount);

        if (annotation != null) {
            if (!StringUtil.isEmpty(annotation.methodId())) {
                function = abi.getFunctionBySign(annotation.methodId());
            } else if (!StringUtil.isEmpty(annotation.name())) {
                function = abi.getFunctionByName(annotation.name(), paramCount);
            }
        }

        if (function == null) {
            throw new RuntimeException("No function found: " + method.getName());
        }
        return function;
    }
}
