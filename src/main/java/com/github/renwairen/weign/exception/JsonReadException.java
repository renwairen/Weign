package com.github.renwairen.weign.exception;

/**
 * @Author Zhang La @Created at 2023/1/28 13:34
 */
public class JsonReadException extends RuntimeException {

  public JsonReadException(Exception e) {
    super(e);
  }
}
