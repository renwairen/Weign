package io.github.renwairen.weign.domain;

import lombok.AllArgsConstructor;

/**
 * @Author Zhang La @Created at 2023/5/15 10:40
 */
@AllArgsConstructor
public class ParamMeta {

    /**
     * 是否是数组
     */
    public final boolean isArray;

    /**
     * 数组长度
     *
     * <p>动态数组，为 null
     */
    public final Integer length;

    /**
     * 数组元素
     *
     * <p>nullable
     */
    public final Param item;
}
