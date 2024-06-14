package io.github.renwairen.weign.util;

import io.github.renwairen.weign.exception.IORuntimeException;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.charset.StandardCharsets;

/**
 * @Author Zhang La @Created at 2022/6/1 13:36
 */
@Slf4j
public abstract class IOUtil {

    public static String toString(URL url) throws IORuntimeException {
        InputStream inputStream = null;
        try {
            inputStream = url.openStream();
            byte[] bytes = inputStream.readAllBytes();
            return new String(bytes, StandardCharsets.UTF_8);
        } catch (IOException e) {
            throw new IORuntimeException(e);
        } finally {
            close(inputStream);
        }
    }

    /**
     * safe close quietly
     *
     * @param closeable nullable
     */
    public static void close(AutoCloseable closeable) {
        if (closeable == null) {
            return;
        }
        try {
            closeable.close();
        } catch (Exception e) {
            // 不做处理
            log.info("Close Error", e);
        }
    }
}
