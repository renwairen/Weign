package io.github.renwairen.weign.exception;

/**
 * @Author Zhang La @Created at 2023/1/29 17:37
 */
public class HttpRequestException extends RuntimeException {

    public HttpRequestException(Exception e) {
        super(e);
    }
}
