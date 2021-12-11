package kr.co.mcedu.config.web;

import kr.co.mcedu.utils.StringUtils;
import org.springframework.util.StreamUtils;

import javax.servlet.ReadListener;
import javax.servlet.ServletInputStream;
import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import java.io.*;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

public class ReadableRequestWrapper extends HttpServletRequestWrapper {
    private final Charset encoding;
    private final byte[] rawData;

    public ReadableRequestWrapper(HttpServletRequest request) throws IOException {
        super(request);
        String characterEncoding = request.getCharacterEncoding();
        if (StringUtils.isEmpty(characterEncoding)) {
            characterEncoding = StandardCharsets.UTF_8.name();
        }
        this.encoding = Charset.forName(
                characterEncoding); // Convert InputStream data to byte array and store it to this wrapper instance.
        InputStream inputStream = request.getInputStream();
        this.rawData = StreamUtils.copyToByteArray(inputStream);
    }

    @Override
    public ServletInputStream getInputStream() throws IOException {
        final ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(this.rawData);
        return new ServletInputStream() {
            @Override
            public boolean isFinished() {
                return false;
            }

            @Override
            public boolean isReady() {
                return false;
            }

            @Override
            public void setReadListener(ReadListener readListener) {
            }

            public int read() throws IOException {
                return byteArrayInputStream.read();
            }
        };
    }

    @Override public BufferedReader getReader() throws IOException {
        return new BufferedReader(new InputStreamReader(this.getInputStream(), this.encoding));
    }

    @Override public ServletRequest getRequest() {
        return super.getRequest();
    }
}
