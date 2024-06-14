package io.github.renwairen.weign.contract;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.github.renwairen.weign.annotation.Contract;
import io.github.renwairen.weign.annotation.Function;
import lombok.Data;

import java.math.BigInteger;

/**
 * @author zhangla
 * @date 2024-06-07 14:51
 */
@Contract("/abi/ERC-20.json")
public interface ERC20 {

    String name();

    @Function(name = "totalSupply")
    BigInteger totalSupply();

    Integer decimals();

    BigInteger balanceOf(String address);

    String symbol();

    /**
     * Withdrawal event
     */
    @Data
    class Withdrawal {

        private String src;

        @JsonProperty("wad")
        private BigInteger wad;
    }
}
