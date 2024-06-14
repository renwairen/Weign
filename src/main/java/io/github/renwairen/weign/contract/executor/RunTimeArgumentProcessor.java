package io.github.renwairen.weign.contract.executor;

import lombok.AllArgsConstructor;

import java.util.Arrays;

/**
 * 运行时根据第一个参数获取上下文和参数信息 @Author Zhang La @Created at 2023/1/12 10:36
 */
@AllArgsConstructor
public class RunTimeArgumentProcessor implements ArgumentProcessor {

    private final ExecuteOption option;

    public ExecuteOption getOption(Object[] args) {
        Object arg = args[0];
        if (arg == null) {
            return option;
        }

        return new ExecuteOption(option, (ExecuteOption) arg);
    }

    public Object[] getArgs(Object[] args) {
        return Arrays.copyOfRange(args, 1, args.length);
    }
}
