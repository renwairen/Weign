package io.github.renwairen.weign.annotation;

import java.lang.annotation.*;

/**
 * 对于同名方法，需要手工指定方法id @Author Zhang La @Created at 2022/6/24 13:40
 */
@Documented
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Function {

    /**
     * 0x3f969cfe
     *
     * @return
     */
    String methodId() default "";

    /**
     * balanceOf
     *
     * @return
     */
    String name() default "";
}
