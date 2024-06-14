package io.github.renwairen.weign.codec.type;

import org.web3j.abi.datatypes.Int;

import java.math.BigInteger;

/**
 * @Author Zhang La @Created at 2022/6/27 16:46
 */
public class LooseInt extends Int {

    public LooseInt(int bitSize, BigInteger value) {
        super(bitSize, value);
    }
}
