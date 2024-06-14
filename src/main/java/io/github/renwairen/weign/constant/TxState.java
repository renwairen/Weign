package io.github.renwairen.weign.constant;

/**
 * transaction状态 @Author Zhang La @Created at 2022/6/2 10:33
 */
public enum TxState {

    /**
     * 发送中，暂定为通过sendTransaction发送后到Pool之前的状态
     */
    WAITING,

    /**
     * 进入 pending 池中，通过PendingTransaction拿到的数据
     */
    PENDING,

    /**
     * 上链，状态成功。 与最新块高度差距 < 1
     */
    PACKAGED_SUCCESS,

    /**
     * 上链，状态失败。 与最新块高度差距 < 1
     */
    PACKAGED_FAILED,

    /**
     * 执行成功。与最新块高度差距 > = 1
     */
    SUCCESS,

    /**
     * 取消 tx，相同address和nonce，后续transaction会取消之前的
     */
    CANCELED,

    /**
     * 执行失败
     */
    FAILED,
    ;
}
