package com.github.renwairen.weign.domain;


import com.github.renwairen.weign.util.ArrayUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Author Zhang La @Created at 2022/7/13 13:08
 */
public class InterfaceContainer<T extends Interface> {

    /**
     * name 有可能重复，方法名称相同，参数不同的是允许的
     */
    private final Map<String, List<T>> nameMap = new HashMap<>();

    private final Map<String, T> signMap = new HashMap<>();

    public InterfaceContainer(List<T> ts) {
        for (T t : ts) {
            String name = t.getName();
            String sign = t.getSign();
            List<T> vs = nameMap.get(name);
            if (vs == null) {
                vs = new ArrayList<>();
                nameMap.put(name, vs);
            }
            vs.add(t);
            signMap.put(sign, t);
        }
    }

    public T getByName(String name) {
        List<T> ts = nameMap.get(name);
        if (ts == null || ts.isEmpty()) {
            return null;
        }
        return ts.get(0);
    }

    /**
     * @param name
     * @param argNo 参数数量
     * @return
     */
    public T getByName(String name, int argNo) {
        List<T> ts = nameMap.get(name);
        if (ts == null || ts.isEmpty()) {
            return null;
        }
        for (T t : ts) {
            if (ArrayUtil.getLength(t.getInputs()) == argNo) {
                return t;
            }
        }
        return null;
    }

    public T getBySign(String sign) {
        return signMap.get(sign);
    }

    public Map<String, T> getSignMap() {
        return new HashMap<>(signMap);
    }
}
