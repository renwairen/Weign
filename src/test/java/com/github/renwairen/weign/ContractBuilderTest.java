package com.github.renwairen.weign;

import com.github.renwairen.weign.contract.ERC20;
import com.github.renwairen.weign.contract.executor.ExecuteOption;
import com.github.renwairen.weign.domain.ABI;
import com.github.renwairen.weign.domain.EventData;
import com.github.renwairen.weign.util.JsonUtil;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.web3j.protocol.core.methods.response.Log;
import org.web3j.protocol.core.methods.response.TransactionReceipt;

import java.io.IOException;
import java.math.BigInteger;
import java.util.Objects;

/**
 * @author zhangla
 * @date 2024-06-07 14:46
 */
class ContractBuilderTest {

    /**
     * WETH
     * <p>https://etherscan.io/token/0xc02aaa39b223fe8d0a0e5c4f27ead9083c756cc2
     */
    @Test
    void erc20() throws IOException {
        //read contract
        ExecuteOption option = ExecuteOption.builder()
                .web3j(Constant.WEB_3_J)
                .contract("0xc02aaa39b223fe8d0a0e5c4f27ead9083c756cc2")
                .build();
        ERC20 erc20 = ContractBuilder.create(option, ERC20.class);
        Assertions.assertEquals("WETH", erc20.symbol());
        Assertions.assertEquals("Wrapped Ether", erc20.name());
        Assertions.assertEquals(18, erc20.decimals());
        Assertions.assertTrue(erc20.balanceOf("0x3AfBAE812F3C29b5926504250888415a01aaC57f").compareTo(BigInteger.ZERO) >= 0);

        //parse event
        //https://etherscan.io/tx/0xfae94b7f014b77cc4dc04d7b1ee523b88198c5cb03dc9e107ede83833c1e4908#eventlog
        TransactionReceipt receipt = Constant.WEB_3_J.ethGetTransactionReceipt("0xfae94b7f014b77cc4dc04d7b1ee523b88198c5cb03dc9e107ede83833c1e4908")
                .send().getTransactionReceipt().get();
        Log log = receipt.getLogs().stream().filter(l -> Objects.equals(BigInteger.valueOf(209), l.getLogIndex()))
                .findAny().get();
        ABI abi = ABIBuilder.clazz(ERC20.class);
        EventData eventData = abi.decodeEvent(log.getTopics(), log.getData());
        ERC20.Withdrawal withdrawal = JsonUtil.read(eventData.getAll(), ERC20.Withdrawal.class);
        Assertions.assertEquals("0x22F9dCF4647084d6C31b2765F6910cd85C178C18".toLowerCase(), withdrawal.getSrc());
        Assertions.assertEquals(new BigInteger("14706622945065745"), withdrawal.getWad());
    }
}