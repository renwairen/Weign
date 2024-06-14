package io.github.renwairen.weign.util;

import lombok.AllArgsConstructor;

import java.lang.ref.SoftReference;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

/**
 * 使用软引用简单实现的一个map，用作缓存
 *
 * <p>线程安全 @Author Zhang La @Created at 2023/6/16 13:16
 */
@AllArgsConstructor
public class SoftHashMap<K, V> {

    private final Map<K, SoftReference<V>> delegate = new HashMap<>();

    private final Function<K, V> function;

    public V get(K key) {
        V value = putIfAbsent(key);
        clear();
        return value;
    }

    private V putIfAbsent(K key) {
        SoftReference<V> reference = this.delegate.get(key);
        V value = null;
        if (reference != null) {
            value = reference.get();
        }

        if (value == null) {
            synchronized (this) {
                // 二次检测
                reference = this.delegate.get(key);
                if (reference != null) {
                    value = reference.get();
                }
                if (value == null) {
                    // 软引用里的值随时可能被回收，所以这里把 value 赋值到局部变量，而不是从软引用中获取，避免被回收获取不到的情况
                    value = this.function.apply(key);
                    reference = new SoftReference<>(value);
                    this.delegate.put(key, reference);
                }
            }
        }

        return value;
    }

    /**
     * 清理已经被垃圾回收的 entry，避免map过大
     *
     * <p>使用的 SoftReference 会在内存不足时，被回收掉
     */
    private void clear() {
        // 先统计需要删除的key
        List<K> keys = new ArrayList<>();
        for (Map.Entry<K, SoftReference<V>> entry : this.delegate.entrySet()) {
            SoftReference<V> reference = entry.getValue();
            if (reference == null || reference.get() == null) {
                keys.add(entry.getKey());
            }
        }

        if (keys.isEmpty()) {
            return;
        }

        for (K key : keys) {
            // 循环遍历，并进行二次检查
            synchronized (this) {
                SoftReference<V> reference = this.delegate.get(key);
                if (reference == null || reference.get() == null) {
                    this.delegate.remove(key);
                }
            }
        }
    }
}
