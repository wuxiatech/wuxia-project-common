package cn.wuxia.project.common.third.baidu.tts;

import java.util.List;
import java.util.Map;

import cn.wuxia.project.common.third.baidu.OpenAccount;
import cn.wuxia.common.util.*;
import org.apache.http.entity.ByteArrayEntity;

import com.alibaba.fastjson.JSONObject;

import cn.wuxia.common.web.httpclient.HttpClientException;
import cn.wuxia.common.web.httpclient.HttpClientRequest;
import cn.wuxia.common.web.httpclient.HttpClientResponse;
import cn.wuxia.common.web.httpclient.HttpClientUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class VoiceTranslate {
    protected final Logger logger = LoggerFactory.getLogger(this.getClass());

    private static final String serverURL = "http://vop.baidu.com/server_api";

    private OpenAccount account;

    /**
     * 用户唯一标识，用来区分用户，计算UV值。建议填写能区分用户的机器 MAC 地址或 IMEI 码
     */
    private String cuid = StringUtil.random(10);

    /**
     * 音频格式
     */
    private String format = "amr";

    /**
     * 音频采样率
     */
    private int rate = 8000;

    public VoiceTranslate(OpenAccount openAccount) {
        this.account = openAccount;
    }

    public static VoiceTranslate build(OpenAccount openAccount) {
        return new VoiceTranslate(openAccount);
    }

    /**
     * 用户唯一标识，用来区分用户，计算UV值。建议填写能区分用户的机器 MAC 地址或 IMEI 码
     */
    public VoiceTranslate cuid(String cuid) {
        if (StringUtil.isNotBlank(cuid)) {
            this.cuid = cuid;
        }
        return this;
    }

    /**
     * 语音格式，pcm 或者 wav 或者 amr。不区分大小写，推荐使用pcm文件
     */
    public VoiceTranslate format(String format) {
        if (StringUtil.isNotBlank(format)) {
            this.format = format;
        }
        return this;
    }

    /**
     * 采样率，支持 8000 或者 16000 ，推荐文件的采样率 16000
     */
    public VoiceTranslate rate(int rate) {
        if (StringUtil.isNotBlank(rate)) {
            this.rate = rate;
        }
        return this;
    }

    /**
     * 500  不支持输入
        501 输入参数不正确
        502 token验证失败
        503 合成后端错误
     */
    public String translate(byte[] bytes) throws HttpClientException {

        JSONObject params = new JSONObject();
        params.put("lan", "zh");
        params.put("token", account.getAccessToken());
        params.put("ctp", "1");
        params.put("cuid", cuid);

        HttpClientResponse resp = HttpClientUtil.execute(
                HttpClientRequest.post(serverURL).addHeader("Content-type", "audio/" + format + ";rate=" + rate).setParam(params),
                new ByteArrayEntity(bytes));
        logger.debug("{}", resp.getCharset());
        resp.setCharset("utf-8");
        Map map = JsonUtil.fromJson(resp.getStringResult());

        logger.debug("语音转文字结果：{}", map);
        List<String> result = (List<String>) MapUtil.getObject(map, "result");
        if (ListUtil.isEmpty(result))
            throw new RuntimeException(MapUtil.getString(map, "err_msg"));
        return result.get(0);
    }
}
