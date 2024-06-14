package io.github.renwairen.weign.codec.decoder;


import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.node.TextNode;
import io.github.renwairen.weign.domain.Param;
import io.github.renwairen.weign.util.ArrayUtil;
import io.github.renwairen.weign.util.JsonUtil;
import io.github.renwairen.weign.util.StringUtil;
import org.web3j.utils.Numeric;

/**
 * @Author Zhang La @Created at 2022/7/13 10:23
 */
public class TopicDecoder {

    protected final Param[] params;

    protected int length;

    public TopicDecoder(Param[] indexeds) {
        this.params = indexeds;
        // indexed + event sign
        this.length = indexeds.length + 1;
    }

    public ObjectNode decode(String[] topics) {
        validate(topics);

        ObjectNode objectNode = JsonUtil.objectNode();
        // i = 0，第一个元素为event签名，解析从 i = 1 开始
        for (int i = 1; i < topics.length; ++i) {
            Param param = params[i - 1];
            JsonNode jsonNode = decode(param, topics[i]);
            objectNode.set(param.getName(), jsonNode);
        }
        return objectNode;
    }

    protected void validate(String[] topics) {
        if (length != ArrayUtil.getLength(topics)) {
            throw new RuntimeException("Invalid topic length");
        }
    }

    protected JsonNode decode(Param param, String topic) {
        if (StringUtil.length(topic) > Decoder.HEX_STRING_LENGTH) {
            topic = Numeric.cleanHexPrefix(topic);
        }
        // topic类型，对于动态数据和超过64个字符的数据，返回的是keccak256的值，这里将数据类型替换为address
        if (param.isDynamic() || param.getHeadLength() > Decoder.HEX_STRING_LENGTH) {
            return TextNode.valueOf(topic);
        }
        return param.decode(topic, 0);
    }
}
