package com.github.renwairen.weign.annotation;

import org.web3j.protocol.core.methods.response.EthSendTransaction;

import java.lang.annotation.*;
import java.util.concurrent.CompletableFuture;

/**
 * 标注接口为合约
 *
 * <p>入参
 *
 * <p>tuple类型可以使用 list, map<arg name, arg value>, java bean
 *
 * <p>对于需要发送transaction的接口，即需要上链的接口，约定返回值为:
 *
 * <p>同步{@link org.web3j.protocol.core.methods.response.TransactionReceipt}
 *
 * <p>异步{@link CompletableFuture<org.web3j.protocol.core.methods.response.TransactionReceipt>} 或
 *
 * <p>同步{@link EthSendTransaction}
 * 只是发送transaction，需要判断返回值是否成功 {@link EthSendTransaction#hasError()}
 *
 * <p>对于只读类型数据，返回值可以为:
 *
 * <p>void
 *
 * <p>返回值为单个，则可以为 byte[], {@link java.math.BigInteger}, {@link String}, {@link Boolean}
 *
 * <p>多个值，可使用java bean，根据参数类型和顺序定义与返回值相同的bean @Author Zhang La @Created at 2022/6/22 16:18
 */
@Documented
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface Contract {

    /**
     * /contract.abi/test.abi
     *
     * @return
     */
    String value();
}
