package com.github.renwairen.weign.codec.encoder;

import lombok.AllArgsConstructor;
import org.web3j.abi.TypeEncoder;
import org.web3j.abi.datatypes.generated.Uint256;

import java.util.ArrayList;
import java.util.List;

/**
 * 编码辅助类
 *
 * <p>对于静态数据，直接平铺
 *
 * <p>对于动态数据，先用0作为占位符，保证长度正确
 *
 * <p>最终toString再计算实际偏移量，替代占位符 @Author Zhang La @Created at 2022/7/7 10:22
 */
public class EncoderContext {

    private static final String PLACEHOLDER = TypeEncoder.encode(new Uint256(0));

    /**
     * left: is static; middle: head; right: tail
     */
    private List<Section> sections = new ArrayList<>();

    /**
     * 动态数据偏移量
     */
    private int offset = 0;

    /**
     * 静态数据
     *
     * @param data
     */
    public void nextStatic(String data) {
        next(true, data, "");
    }

    /**
     * 动态数据
     *
     * @param data
     */
    public void nextDynamic(String data) {
        // 占位符
        next(false, PLACEHOLDER, data);
    }

    private void next(boolean isStatic, String head, String tail) {
        offset += head.length();
        sections.add(new Section(isStatic, head, tail));
    }

    public String toString() {
        StringBuilder builder = new StringBuilder();
        // head
        for (Section section : sections) {
            String head = section.head;
            if (!section.isStatic) {
                // dynamic
                head = TypeEncoder.encode(new Uint256(offset / 2));
                offset += section.tail.length();
            }
            builder.append(head);
        }

        // tail
        for (Section section : sections) {
            builder.append(section.tail);
        }
        return builder.toString();
    }

    @AllArgsConstructor
    private static class Section {

        final boolean isStatic;

        final String head;

        final String tail;
    }
}
