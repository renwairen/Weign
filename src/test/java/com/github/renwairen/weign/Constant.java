package com.github.renwairen.weign;

import org.web3j.protocol.Web3j;
import org.web3j.protocol.http.HttpService;

/**
 * @author zhangla
 * @date 2024-06-07 14:59
 */
public abstract class Constant {

    public static final String PROVIDER_URL = "https://eth-mainnet.g.alchemy.com/v2/NxkUAJJIkPja9g47jhak-Azr745_cqBS";

    public static final Web3j WEB_3_J = Web3j.build(new HttpService(PROVIDER_URL));
}
