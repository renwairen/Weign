package io.github.renwairen.weign.exception;

import java.io.IOException;

/**
 * @Author Zhang La @Created at 2023/6/16 10:51
 */
public class IORuntimeException extends RuntimeException {

    public IORuntimeException(IOException e) {
        super(e);
    }
}
