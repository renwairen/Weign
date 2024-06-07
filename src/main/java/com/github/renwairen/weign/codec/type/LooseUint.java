package com.github.renwairen.weign.codec.type;

import org.web3j.abi.datatypes.Uint;

import java.math.BigInteger;

/**
 * @Author Zhang La @Created at 2022/6/27 16:40
 */
public class LooseUint extends Uint {

    public LooseUint(int bitSize, BigInteger value) {
        super(bitSize, value);
    }
}
