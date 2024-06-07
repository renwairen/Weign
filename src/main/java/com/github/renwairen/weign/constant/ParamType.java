package com.github.renwairen.weign.constant;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * @Author Zhang La @Created at 2022/7/6 17:01
 */
@Getter
@AllArgsConstructor
public enum ParamType {
    TUPLE("tuple"),

    STRING("string"),

    BYTES("bytes"),
    ;

    final String value;

    public static final Set<String> DYNAMIC_TYPES =
            new HashSet<>(Arrays.asList(STRING.value, BYTES.value));
}
