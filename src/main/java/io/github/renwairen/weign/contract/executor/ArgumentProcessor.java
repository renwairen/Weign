package io.github.renwairen.weign.contract.executor;

/**
 * @Author Zhang La @Created at 2023/1/12 10:36
 */
public interface ArgumentProcessor {

    /**
     * 根据参数获取运行上下文
     *
     * @param args
     * @return
     */
    public ExecuteOption getOption(Object[] args);

    /**
     * 根据方法参数获取实际链上运行参数
     *
     * @param args
     * @return
     */
    public Object[] getArgs(Object[] args);
}
