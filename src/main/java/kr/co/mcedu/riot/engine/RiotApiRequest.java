package kr.co.mcedu.riot.engine;

import kr.co.mcedu.utils.StringUtils;
import lombok.Getter;
import lombok.Setter;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

@Getter
@Setter
public class RiotApiRequest {

    private String messageKey = StringUtils.randomStringGenerate(6);
    private RiotApiType apiType = RiotApiType.DEFAULT;
    private Map<String, Object> data = new HashMap<>();
    private String url = "";

    public void build() {
        String urlTemplate = apiType.getUrl();
        String encodedValue = "";
        for (Map.Entry<String, Object> entry : data.entrySet()) {
            String key = entry.getKey();
            Object value = entry.getValue();
            try {
                encodedValue = URLEncoder.encode(value.toString(), StandardCharsets.UTF_8.toString());
            } catch (UnsupportedEncodingException ignore) {
                encodedValue = value.toString();
            }
            urlTemplate = urlTemplate.replace("{" + key + "}", encodedValue);
        }
        this.url = urlTemplate;
    }
}
