package com.github.renwairen.weign.contract.executor;


import com.fasterxml.jackson.databind.JsonNode;
import com.github.renwairen.weign.domain.Function;
import com.github.renwairen.weign.util.FunctionUtil;
import com.github.renwairen.weign.util.StringUtil;

import java.io.IOException;
import java.lang.reflect.Method;

/**
 * 只读，不发送交易，同步等待线上返回值 @Author Zhang La @Created at 2022/7/13 14:16
 */
public class MethodExecutor {

    private final ArgumentProcessor processor;

    private final FunctionExecutor executor;

    private final ResultDecoder<?> decoder;

    public MethodExecutor(ExecuteOption option, Method method, Function function) {
        // 按照方法参数区分编码
        // 如果方法首个参数为 ExecuteOption，则将此参数作为上下文处理，后续参数参与链上执行
        if (FunctionUtil.isRunTimeOption(method)) {
            this.processor = new RunTimeArgumentProcessor(option);
        } else {
            this.processor = new OriginalArgumentProcessor(option);
        }
        this.executor = new FunctionExecutor(function);
        this.decoder = new ResultDecoder(function, method);
    }

    public Object execute(Object[] args) throws IOException {
        ExecuteOption option = processor.getOption(args);
        if (StringUtil.isEmpty(option.getContract())) {
            throw new NullPointerException("ExecuteOption#contract cannot be null or empty");
        }
        args = processor.getArgs(args);
        JsonNode jsonNode = executor.execute(option, args);
        return decoder.decode(jsonNode);
    }
}
