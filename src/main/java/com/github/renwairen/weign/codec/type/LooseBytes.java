package com.github.renwairen.weign.codec.type;

import org.web3j.abi.datatypes.Bytes;

/**
 * @Author Zhang La @Created at 2022/6/27 17:26
 */
public class LooseBytes extends Bytes {

    public LooseBytes(int byteSize, byte[] value) {
        super(byteSize, value);
    }
}
