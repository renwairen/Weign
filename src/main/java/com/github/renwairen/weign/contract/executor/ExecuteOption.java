package com.github.renwairen.weign.contract.executor;

import lombok.*;
import lombok.experimental.SuperBuilder;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.DefaultBlockParameter;
import org.web3j.protocol.core.DefaultBlockParameterName;

import java.util.function.Supplier;

/** 默认执行配置 @Author Zhang La @Created at 2022/6/24 13:19 */
@Getter
@ToString
@SuperBuilder
@EqualsAndHashCode
@AllArgsConstructor
public class ExecuteOption {

  /** 合约地址 */
  private String contract;

  private Web3j web3j;

  /**
   * 如果 {@link ExecuteOption#web3j} 不为 null, 则使用 web3j
   *
   * <p>引入 supplier 的目的主要是为了运行时动态获取 web3j 实例，对于故障转移、结点动态配置等更友好
   */
  private Supplier<Web3j> web3jSupplier;

  /**
   * 钱包地址，用于 transaction 的 from
   *
   * <p>如果未指定，则使用默认地址
   */
  @Builder.Default private String wallet = "0x8dc847af872947ac18d5d63fa646eb65d4d99560";

  /** 默认使用最新块 */
  @Builder.Default private DefaultBlockParameter blockParameter = DefaultBlockParameterName.LATEST;

  /**
   * 以 main 为主，当 arg 中有非空值，则使用 arg
   *
   * @param main nonnull
   * @param arg nullable
   */
  public ExecuteOption(ExecuteOption main, ExecuteOption arg) {
    this(main.contract, main.web3j, main.web3jSupplier, main.wallet, main.blockParameter);
    if (arg != null) {
      if (arg.contract != null && !arg.contract.isEmpty()) {
        this.contract = arg.contract;
      }
      if (arg.web3j != null) {
        this.web3j = arg.web3j;
      }
      if (arg.web3jSupplier != null) {
        this.web3jSupplier = arg.web3jSupplier;
      }
      if (arg.wallet != null && !arg.wallet.isEmpty()) {
        this.wallet = arg.wallet;
      }
      if (arg.blockParameter != null) {
        this.blockParameter = arg.blockParameter;
      }
    }
  }

  /**
   * 如果 {@link ExecuteOption#web3j} 不为 null，则使用；否则使用 {@link ExecuteOption#web3jSupplier} 生成
   *
   * @return
   */
  public Web3j genWeb3j() {
    if (web3j != null) {
      return web3j;
    }
    if (web3jSupplier == null) {
      throw new NullPointerException("web3j, web3jSupplier cannot be all null");
    }
    return web3jSupplier.get();
  }
}
