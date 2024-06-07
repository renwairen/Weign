package com.github.renwairen.weign.codec.type;

import org.web3j.abi.datatypes.Fixed;

import java.math.BigInteger;

/**
 * @Author Zhang La @Created at 2022/6/27 17:25
 */
public class LooseFixed extends Fixed {

    public LooseFixed(int mBitSize, int nBitSize, BigInteger value) {
        super(mBitSize, nBitSize, value);
    }
}
