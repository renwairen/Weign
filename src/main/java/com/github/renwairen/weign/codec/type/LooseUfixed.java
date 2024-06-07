package com.github.renwairen.weign.codec.type;

import org.web3j.abi.datatypes.Ufixed;

import java.math.BigInteger;

/**
 * @Author Zhang La @Created at 2022/6/27 17:26
 */
public class LooseUfixed extends Ufixed {

    public LooseUfixed(int mBitSize, int nBitSize, BigInteger value) {
        super(mBitSize, nBitSize, value);
    }
}
