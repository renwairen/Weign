package com.github.renwairen.weign;


import com.github.renwairen.weign.contract.executor.ContractInvocationHandler;
import com.github.renwairen.weign.contract.executor.ExecuteOption;
import com.github.renwairen.weign.domain.ABI;
import lombok.Data;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Proxy;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 合约工厂类，用于实例化合约接口 @Author Zhang La @Created at 2022/6/21 10:53
 */
public abstract class ContractBuilder {

    private static final Map<Key, ABI> ABI_MAP = new ConcurrentHashMap<>();

    /**
     * @param option nullable
     * @param clazz
     * @return
     */
    public static <T> T create(ExecuteOption option, Class<T> clazz) {
        Key key = new Key(clazz, "");
        ABI abi = ABI_MAP.computeIfAbsent(key, k -> ABIBuilder.clazz(clazz));
        return create(option, clazz, abi);
    }

    /**
     * @param option
     * @param clazz
     * @return
     */
    public static <T> T create(ExecuteOption option, Class<T> clazz, String json) {
        Key key = new Key(clazz, json);
        ABI abi = ABI_MAP.computeIfAbsent(key, k -> ABIBuilder.json(json));
        return create(option, clazz, abi);
    }

    private static <T> T create(ExecuteOption option, Class<?> clazz, ABI abi) {
        InvocationHandler invocationHandler = new ContractInvocationHandler(option, clazz, abi);
        ClassLoader classLoader = invocationHandler.getClass().getClassLoader();
        Class<?>[] interfaces = new Class[]{clazz};
        return (T) Proxy.newProxyInstance(classLoader, interfaces, invocationHandler);
    }

    @Data
    private static class Key {

        final Class<?> clazz;

        final String json;
    }
}
