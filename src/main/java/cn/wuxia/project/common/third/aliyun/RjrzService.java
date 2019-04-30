package cn.wuxia.project.common.third.aliyun;

import cn.wuxia.aliyun.components.ApiAccount;
import cn.wuxia.common.web.httpclient.*;
import cn.wuxia.project.common.third.aliyun.bean.RjrzParam;
import cn.wuxia.project.common.third.aliyun.bean.RjrzResponse;
import com.alibaba.fastjson.JSONObject;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.time.FastDateFormat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

public class RjrzService {
    Logger logger = LoggerFactory.getLogger(getClass());
    private ApiAccount account;
    private String timeFormat = "yyyy-MM-dd'T'HH:mm:ssZ";
    private String version = "2016-11-23";
    private String signatureMethod = "HMAC-SHA1";
    private String signatureVersion = "1.0";
    public final FastDateFormat ISO_8601_EXTENDED_DATETIME_FORMAT = FastDateFormat.getInstance(timeFormat);
    private HttpAction url = new HttpAction("http://jaq.aliyuncs.com/", HttpClientMethod.GET);

    public RjrzService(ApiAccount account) {
        this.account = account;
    }

    public static RjrzService build(ApiAccount apiAccount) {
        return new RjrzService(apiAccount);
    }

    /**
     * android & ios 人机认证
     *
     * @param session
     * @return
     * @throws Exception
     */
    public RjrzResponse renzheng(String session) throws Exception {
        RjrzParam param = new RjrzParam();
        param.setAction("AfsAppCheck");
        param.setSession(session);
        param.setAccessKeyId(account.getAppKey());
        param.setFormat("JSON");
        param.setVersion(version);
        param.setSignatureMethod(signatureMethod);
        param.setTimestamp(getISO8601Timestamp(new Date()));
        param.setSignatureVersion(signatureVersion);
        param.setSignatureNonce(RandomStringUtils.randomAlphanumeric(32));

        /**
         * 生成签名
         */
        try {
            String signature = RjrjSignUtil.sign(account.getAppSecret(), url.getMethod().name(), param);
            param.setSignature(signature);
        } catch (Exception e) {
            logger.error("", e);
            throw new RuntimeException("生成签名错误");
        }
        /**
         * 请求url
         */
        HttpClientRequest request = HttpClientRequest.create(url);
        request.addParam(param.getMap());
        try {
            HttpClientResponse response = request.execute();
            //{"Data":{"SecondCheckResult":2},"ErrorMsg":"success","ErrorCode":0}
            //{"ErrorMsg":"[sessionId] is invalid","ErrorCode":400}
            return JSONObject.parseObject(response.getStringResult(), RjrzResponse.class);
        } catch (HttpClientException e) {
            throw new RuntimeException("请求错误");
        }

    }

    /**
     * 传入Data类型日期，返回字符串类型时间（ISO8601标准时间）
     *
     * @param date
     * @return
     */
    public static String getISO8601Timestamp(Date date) {
        TimeZone tz = TimeZone.getTimeZone("UTC");
//        TimeZone tz = TimeZone.getTimeZone("Asia/Shanghai");
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
        df.setTimeZone(tz);
        String nowAsISO = df.format(date);
        return nowAsISO;
    }
}
