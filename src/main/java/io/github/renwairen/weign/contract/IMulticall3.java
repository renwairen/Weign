package io.github.renwairen.weign.contract;


import io.github.renwairen.weign.ContractBuilder;
import io.github.renwairen.weign.annotation.Contract;
import io.github.renwairen.weign.contract.executor.ExecuteOption;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NonNull;

import java.math.BigInteger;

/**
 * https://github.com/mds1/multicall
 *
 * <p>合约里的 getBlockHash 方法，经测试过一段时间之后会返回 0，不能使用 @Author Zhang La @Created at 2023/1/11 10:35
 */
@Contract("/contract.abi/Multicall3.json")
public interface IMulticall3 {

    /**
     * https://github.com/mds1/multicall
     *
     * <p>各个链一样
     */
    String ADDRESS = "0xcA11bde05977b3631167028862bE2a173976CA11";

    /**
     * IMulticall3 实例
     */
    IMulticall3 instance =
            ContractBuilder.create(
                    ExecuteOption.builder().contract(IMulticall3.ADDRESS).build(), IMulticall3.class);

    /**
     * @param option 使用时，需设置 web3j 或 web3jSupplier
     * @param calls
     * @return
     */
    Result[] aggregate3(ExecuteOption option, Call3... calls);

    /**
     * @param option 使用时，需设置 web3j 或 web3jSupplier
     * @return
     */
    BigInteger getBlockNumber(ExecuteOption option);

    @Getter
    @Builder
    class Call3 {

        @NonNull
        private String target;

        @NonNull
        @Builder.Default
        private Boolean allowFailure = true;

        @NonNull
        @Builder.Default
        private byte[] callData = new byte[0];
    }

    @Data
    class Result {

        private Boolean success;

        private byte[] returnData;
    }
}
