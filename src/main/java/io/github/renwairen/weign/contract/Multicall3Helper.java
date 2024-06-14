package io.github.renwairen.weign.contract;


import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import io.github.renwairen.weign.contract.executor.ExecuteOption;
import io.github.renwairen.weign.contract.executor.ResultDecoder;
import io.github.renwairen.weign.domain.Function;
import io.github.renwairen.weign.util.JsonUtil;
import io.github.renwairen.weign.util.StringUtil;
import lombok.Builder;
import lombok.Getter;
import org.web3j.utils.Numeric;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * 线程安全 @Author Zhang La @Created at 2023/1/12 11:33
 */
public class Multicall3Helper {

    /**
     * 单例化，避免频繁反射生成带来的性能损失
     */
    private static final IMulticall3 multicall3 = IMulticall3.instance;

    private final ExecuteOption option;

    /**
     * @param option nonnull
     */
    public Multicall3Helper(ExecuteOption option) {
        Objects.requireNonNull(option, "option cannot be null");
        if (StringUtil.isEmpty(option.getContract())) {
            option =
                    new ExecuteOption(
                            IMulticall3.ADDRESS,
                            option.genWeb3j(),
                            option.getWeb3jSupplier(),
                            option.getWallet(),
                            option.getBlockParameter());
        }
        this.option = option;
    }

    /**
     * @param calls
     * @return
     */
    public List<Result> execute(Call... calls) {
        IMulticall3.Call3[] call3s = new IMulticall3.Call3[calls.length];
        for (int i = 0; i < calls.length; ++i) {
            Call call = calls[i];
            String data = call.function.encode(call.args);
            byte[] callData = Numeric.hexStringToByteArray(data);
            call3s[i] =
                    IMulticall3.Call3.builder()
                            .allowFailure(true)
                            .target(call.target)
                            .callData(callData)
                            .build();
        }

        IMulticall3.Result[] results = multicall3.aggregate3(option, call3s);
        List<Result> rets = new ArrayList<>(results.length);
        for (int i = 0; i < results.length; ++i) {
            IMulticall3.Result result = results[i];
            rets.add(new Result(result, calls[i].function));
        }
        return rets;
    }

    @Getter
    public static class Result {

        private final boolean success;

        private final byte[] returnData;

        private final Function function;

        public Result(IMulticall3.Result result, Function function) {
            this.success = result.getSuccess();
            this.returnData = result.getReturnData();
            this.function = function;
        }

        public boolean isEmpty() {
            return returnData == null || returnData.length == 0;
        }

        /**
         * 返回原始数据编码为 json 后的数据
         *
         * @return nonull
         */
        public JsonNode decode() {
            if (isEmpty()) {
                return JsonUtil.nullNode();
            }
            return function.decode(Numeric.toHexString(returnData));
        }

        public <T> T decode(Class<T> clazz) {
            ResultDecoder<T> decoder = new ResultDecoder<T>(function, clazz);
            return decoder.decode(Numeric.toHexString(returnData));
        }

        public <T> T decode(TypeReference<T> reference) {
            ResultDecoder<T> decoder = new ResultDecoder<T>(function, reference);
            return decoder.decode(Numeric.toHexString(returnData));
        }
    }

    @Getter
    @Builder
    public static class Call {

        private final Function function;

        private final String target;

        private final Object[] args;

        public Call(Function function, String target, Object... args) {
            this.function = function;
            this.target = target;
            this.args = args;
        }
    }
}
