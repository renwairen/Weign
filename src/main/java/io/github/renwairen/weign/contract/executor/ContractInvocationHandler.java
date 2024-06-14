package io.github.renwairen.weign.contract.executor;


import io.github.renwairen.weign.domain.ABI;
import io.github.renwairen.weign.domain.Function;
import io.github.renwairen.weign.util.FunctionUtil;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

/**
 * @Author Zhang La @Created at 2022/6/23 19:39
 */
public class ContractInvocationHandler implements InvocationHandler {

    private final Map<Method, MethodExecutor> methodMap = new HashMap<>();

    public ContractInvocationHandler(ExecuteOption option, Class<?> clazz, ABI abi) {
        Method[] methods = clazz.getMethods();
        for (Method method : methods) {
            if (method.getDeclaringClass() == Object.class) {
                continue;
            }
            Function function = FunctionUtil.getFunction(abi, method);
            MethodExecutor executor = new MethodExecutor(option, method, function);
            methodMap.put(method, executor);
        }
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Exception {
        MethodExecutor executor = methodMap.get(method);
        if (executor == null) {
            // equals hashCode toString
            return method.invoke(proxy, args);
        }
        return executor.execute(args);
    }
}
