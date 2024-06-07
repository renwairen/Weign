package com.github.renwairen.weign.contract.executor;

import lombok.AllArgsConstructor;

/** 使用原始运行上下文和参数 @Author Zhang La @Created at 2023/1/12 10:36 */
@AllArgsConstructor
public class OriginalArgumentProcessor implements ArgumentProcessor {

  private final ExecuteOption option;

  public ExecuteOption getOption(Object[] args) {
    return option;
  }

  public Object[] getArgs(Object[] args) {
    return args;
  }
}
