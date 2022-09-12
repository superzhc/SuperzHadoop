package com.github.superzhc.data.utils;

import com.github.superzhc.common.http.HttpRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDateTime;
import java.util.Map;

/**
 * @author superz
 * @create 2022/4/21 15:07
 **/
public class XueQiuUtils {
    private static final Logger log = LoggerFactory.getLogger(XueQiuUtils.class);

    public static final String UA =
            "Mozilla/5.0 (iPhone; CPU iPhone OS 13_2_3 like Mac OS X) AppleWebKit/605.1.15 (KHTML, like Gecko) Version/13.0.3 Mobile/15E148 Safari/604.1 Edg/94.0.4606.71"
            //"Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/80.0.3987.149 Safari/537.36"
            ;

    private static volatile String COOKIES = null;
    private static volatile LocalDateTime EXPIRED = null;

    /**
     * 建议多次复用获取的 cookies，不要每次都去调用这个方法
     *
     * @return
     */
    public static synchronized String cookies() {
        if (null == COOKIES || EXPIRED.isBefore(LocalDateTime.now())) {
            Map<String, String> map = HttpRequest.get("https://xueqiu.com").userAgent(UA).cookies();
            String xqAToken = map.get("xq_a_token");
            COOKIES = String.format("xq_a_token=%s", xqAToken);
            EXPIRED = LocalDateTime.now().plusMinutes(10);
        } else {
            EXPIRED = LocalDateTime.now().plusMinutes(10);
        }
        log.debug("XueQiu Cookies[{},{}]", EXPIRED, COOKIES);
        return COOKIES;
    }
}
