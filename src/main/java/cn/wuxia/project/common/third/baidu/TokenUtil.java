package cn.wuxia.project.common.third.baidu;

import java.io.IOException;
import java.util.Date;
import java.util.Map;

import cn.wuxia.common.mapper.JacksonMapper;
import cn.wuxia.common.web.httpclient.HttpClientResponse;
import org.apache.commons.collections4.MapUtils;
import org.springframework.util.Assert;

import cn.wuxia.common.util.DateUtil;
import cn.wuxia.common.util.NumberUtil;
import cn.wuxia.common.util.StringUtil;
import cn.wuxia.common.web.httpclient.HttpClientException;
import cn.wuxia.common.web.httpclient.HttpClientRequest;

/**
 * 
 * [ticket id] 获取token
 * 
 * @author songlin.li @ Version : V<Ver.No> <2015年4月1日>
 */
public class TokenUtil extends BaseUtil {

    private final static String CACHE_ACCESS_TOKEN_KEY = "cache_access_token";

    private final static String CACHE_PREV_TOKEN_TIME_KEY = "cache_prev_token_time";

    /**
     * 获取access_token
     * 
     * @author songlin
     * @return
     * @throws IOException
     */
    public static String getAccessToken(OpenAccount account) {
        Assert.notNull(account, "account不能为空");
        Assert.notNull(account.getAppid(), "appid不能为空");
        Assert.notNull(account.getAppKey(), "appkey不能为空");
        Assert.notNull(account.getAppSecret(), "appsecret不能为空");

        logger.info("请求时间1：" + System.currentTimeMillis());
        String accessToken = (String) getOutCache(account.getAppid(), CACHE_ACCESS_TOKEN_KEY);
        System.out.println("access_token:" + accessToken);
        Long prevTime = NumberUtil.toLong(getOutCache(account.getAppid(), CACHE_PREV_TOKEN_TIME_KEY), 0L);
        System.out.println("prev_time:" + DateUtil.defaultFormatTimeStamp(new Date(prevTime)));
        Long nowTime = DateUtil.newInstanceDate().getTime();
        System.out.println("now_time:" + DateUtil.defaultFormatTimeStamp(new Date(nowTime)));
        Long between = (nowTime - prevTime) / 1000;
        if (between < 30 * 24 * 60 * 60 && StringUtil.isNotBlank(accessToken)) { // 30天更新一次
            logger.info("ASSCESS_TOKEN在有效期内{} < 2592000 return {}", between, accessToken);
            return accessToken;
        }
        logger.info("请求时间2：" + System.currentTimeMillis());
        logger.info("ASSCESS_TOKEN重新获取{} > 2592000 或 access_token , {}", between, accessToken);

        String getTokenURL = "https://openapi.baidu.com/oauth/2.0/token?grant_type=client_credentials" + "&client_id=" + account.getAppKey()
                + "&client_secret=" + account.getAppSecret();

        Map result;
        try {
            HttpClientResponse response = HttpClientRequest.get(getTokenURL).execute();
            result = JacksonMapper.nonEmptyMapper().getMapper().readValue(response.getByteResult(), Map.class);
        } catch (HttpClientException | IOException e) {
            throw new RuntimeException("获取access_token有误:", e);
        }
        String token = MapUtils.getString(result, "access_token");
        if (StringUtil.isBlank(token)) {
            logger.error("{}", result);
            throw new RuntimeException("获取access_token有误:" + result);
        }
        putInCache(account.getAppid(), CACHE_PREV_TOKEN_TIME_KEY, DateUtil.newInstanceDate().getTime());
        putInCache(account.getAppid(), CACHE_ACCESS_TOKEN_KEY, token);
        Assert.isTrue(StringUtil.equals(token, (String) getOutCache(account.getAppid(), CACHE_ACCESS_TOKEN_KEY)), "access_token值不相同");
        logger.info("请求时间3：" + System.currentTimeMillis());
        return token;
    }

}
