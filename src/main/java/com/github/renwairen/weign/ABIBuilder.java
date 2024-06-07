package com.github.renwairen.weign;


import com.fasterxml.jackson.core.type.TypeReference;
import com.github.renwairen.weign.annotation.Contract;
import com.github.renwairen.weign.domain.ABI;
import com.github.renwairen.weign.domain.InterfaceDescriptor;
import com.github.renwairen.weign.util.IOUtil;
import com.github.renwairen.weign.util.JsonUtil;
import com.github.renwairen.weign.util.SoftHashMap;

import java.net.URL;
import java.util.List;

/**
 * @Author Zhang La @Created at 2022/7/7 15:01
 */
public abstract class ABIBuilder {

    /**
     * abi 文件对应的 java 类型
     */
    private static final TypeReference<List<InterfaceDescriptor>> TYPE_REFERENCE =
            new TypeReference<>() {
            };

    /**
     * json -> abi
     */
    private static final SoftHashMap<String, ABI> ABI_MAP =
            new SoftHashMap<>(
                    key -> {
                        List<InterfaceDescriptor> descriptors = JsonUtil.read(key, TYPE_REFERENCE);
                        return new ABI(descriptors);
                    });

    /**
     * resource path -> json
     */
    private static final SoftHashMap<String, String> RESOURCE_MAP =
            new SoftHashMap<>(
                    key -> {
                        URL url = ABIBuilder.class.getResource(key);
                        if (url == null) {
                            throw new NullPointerException("No file found: " + key);
                        }
                        return IOUtil.toString(url);
                    });

    /**
     * class -> resource path
     */
    private static final SoftHashMap<Class<?>, String> CLASS_MAP =
            new SoftHashMap<>(
                    key -> {
                        Contract contract = key.getAnnotation(Contract.class);
                        if (contract == null) {
                            throw new RuntimeException("Class require @Contract annotation: " + key.getName());
                        }
                        return contract.value();
                    });

    /**
     * @param json abi内容
     * @return
     */
    public static ABI json(String json) {
        return ABI_MAP.get(json);
    }

    /**
     * 从resources目录文件中解析，以 / 开头
     *
     * @param path
     * @return
     */
    public static ABI resource(String path) {
        String json = RESOURCE_MAP.get(path);
        return json(json);
    }

    /**
     * 从类注解的 @Contract 中解析
     *
     * @param clazz
     * @return
     */
    public static ABI clazz(Class<?> clazz) {
        String path = CLASS_MAP.get(clazz);
        return resource(path);
    }
}
