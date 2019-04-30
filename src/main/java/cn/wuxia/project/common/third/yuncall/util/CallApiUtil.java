package cn.wuxia.project.common.third.yuncall.util;

import java.util.Date;
import java.util.Map;
import java.util.Properties;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.collections.MapUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.Maps;

import cn.wuxia.common.util.*;
import cn.wuxia.common.web.httpclient.HttpClientException;
import cn.wuxia.common.web.httpclient.HttpClientRequest;
import cn.wuxia.common.web.httpclient.HttpClientResponse;

/**
 * 本util用户请求 api接口的参数签名
 *
 * @author songlin
 */
public class CallApiUtil {
    private final static Logger logger = LoggerFactory.getLogger(CallApiUtil.class);

    private static Properties prop = PropertiesUtils.loadProperties("classpath:/call.config.properties");

    private static final int SEND_TIMEOUT = NumberUtil.toInteger(prop.getProperty("call.api.sendtimeout"), 3000);

    private static final String API_GATEWAY = prop.getProperty("call.api.gateway");

    private static final String EVENT_GATEWAY = prop.getProperty("call.event.gateway");

    private static final String API_USERID = prop.getProperty("call.api.userid");

    private static final String API_SECRECT = prop.getProperty("call.api.secrect");

    /**
     * call2call用07
     * 虚拟服务号
     */
    public static final String SERVICENO07 = "02010000007";

    /**
     * message2call双向通话用09
     * 播放语音通知
     */
    public static final String SERVICENO09 = "02010000009";

    private static String authorization() {
        String timestramp = DateUtil.dateToString(new Date(), DateUtil.DateFormatter.FORMAT_YYYYMMDDHHMMSS);
        StringBuffer auth = new StringBuffer(API_USERID).append(":").append(timestramp);
        return Base64.encodeBase64String(auth.toString().getBytes());
    }

    private static String sign() {
        String timestramp = DateUtil.dateToString(new Date(), DateUtil.DateFormatter.FORMAT_YYYYMMDDHHMMSS);
        StringBuffer sign = new StringBuffer(API_USERID).append(API_SECRECT).append(timestramp);
        return EncodeUtils.md5ByHex(sign.toString());
    }

    /**
     * @param mobile
     * @param message
     * @return
     * @throws HttpClientException
     * @author songlin
     */
    public static Map<String, Object> sendMessage2Call(String mobile, String message) throws HttpClientException {
        Map<String, String> param = Maps.newHashMap();
        param.put("text", message);
        return webCall(SERVICENO09, mobile, param);
    }

    /**
     * @param mobile
     * @param mobile2
     * @return
     * @throws HttpClientException
     * @author songlin
     */
    public static Map<String, Object> sendCall2Call(String mobile, String mobile2) throws HttpClientException {
        Map<String, String> param = Maps.newHashMap();
        param.put("phoneNum", mobile2);
        return webCall(SERVICENO07, mobile, param);
    }

    /**
     * @param param
     * @param method
     * @return Message=3是没有接听成功，4才是成功
     * @throws HttpClientException
     * @author songlin
     */
    public static Map<String, Object> webCall(String serviceNo, String mobile, Map<String, String> variable) throws HttpClientException {
        if (StringUtil.isBlank(API_USERID) || StringUtil.isBlank(EVENT_GATEWAY)) {
            logger.warn("无法使用电话呼叫功能！");
            return null;
        }
        Map<String, Object> param = Maps.newHashMap();
        param.put("ACCOUNTID", API_USERID);
        param.put("SERVICENO", serviceNo);
        param.put("mobile", mobile);
        String url = EVENT_GATEWAY + StringUtil.replaceKeysSimple(param, CallApiMethodEnum.EVENT_WEBCALL.getUrl());
        if (MapUtil.isNotEmpty(variable)) {
            StringBuffer vars = new StringBuffer("&Variable=");
            for (Map.Entry<String, String> var : variable.entrySet()) {
                vars.append(var.getKey()).append(":").append(var.getValue()).append(",");
            }
            String urlvar = StringUtil.endsWith(vars.toString(), ",") ? StringUtil.substringBeforeLast(vars.toString(), ",") : vars.toString();
            logger.info(urlvar);
            url += urlvar;
        }
        HttpClientRequest req = HttpClientRequest.create(url).setMethod(CallApiMethodEnum.EVENT_WEBCALL.getMethod()).addHeader("Authorization",
                authorization());
        HttpClientResponse resp = req.setConnectionTimeout(SEND_TIMEOUT).execute();
        logger.debug("===={}", resp.getStringResult());
        Map<String, Object> rest = JsonUtil.fromJson(resp.getStringResult());
        if (!MapUtil.getBoolean(rest, "Succeed")) {
            throw new HttpClientException(MapUtil.getString(rest, "Message"));
        }
        return rest;
    }

    public static void dialout(CallAccount account, String mobile) throws HttpClientException {
        Map<String, String> param = Maps.newHashMap();
        param.put("FromExten", account.jobNo);
        param.put("Exten", mobile);
        post2Callcenter(param, CallApiMethodEnum.API_CALL_DIALOUT);
    }

    /**
     * @param param
     * @param method
     * @return
     * @throws HttpClientException
     * @author songlin
     */
    public static Map<String, Object> post2Callcenter(Map<String, String> param, CallApiMethodEnum method) throws HttpClientException {
        if (StringUtil.isBlank(API_USERID) || StringUtil.isBlank(EVENT_GATEWAY)) {
            logger.warn("无法使用电话呼叫功能！");
            return null;
        }
        param.put("ACCOUNTID", API_USERID);
        param.put("SIGN", sign());
        String url = API_GATEWAY + StringUtil.replaceKeysSimple(param, method.getUrl());
        HttpClientRequest req = HttpClientRequest.create(url).setMethod(method.getMethod()).addHeader("Authorization", authorization());
        if (MapUtil.isNotEmpty(param)) {
            req.addParam(param);
        }
        HttpClientResponse resp = req.setConnectionTimeout(SEND_TIMEOUT).execute();
        logger.debug("===={}", resp.getStringResult());
        Map<String, Object> rest = JsonUtil.fromJson(resp.getStringResult());
        if (!MapUtil.getBoolean(rest, "Succeed")) {
            throw new HttpClientException(MapUtil.getString(rest, "Message"));
        }else if(MapUtils.getInteger(rest, "Message")  == 3){
            /**
             * 如果为3则重拨一次，仅重试2次
             */
            post2Callcenter(param, method);
        }
        return rest;
    }
}
