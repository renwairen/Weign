package com.github.renwairen.weign;

import org.web3j.protocol.Web3j;
import org.web3j.protocol.http.HttpService;

/**
 * @author zhangla
 * @date 2024-06-07 14:59
 */
public abstract class Constant {

    public static final String WEB_3_URL = "WEB_3_URL";
    
    public static final Web3j WEB_3_J;

    static {
        String url = System.getenv(WEB_3_URL);
        if (url != null && !url.isEmpty()) {
            WEB_3_J = Web3j.build(new HttpService(url));
        } else {
            WEB_3_J = null;
        }
    }
}
