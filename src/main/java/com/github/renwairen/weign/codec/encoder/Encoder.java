package com.github.renwairen.weign.codec.encoder;

import com.fasterxml.jackson.databind.JsonNode;

/**
 * @Author Zhang La @Created at 2022/7/12 15:46
 */
public interface Encoder {

    String encode(JsonNode arg);
}
