package cn.wuxia.project.common.third.baidu.tts;

import java.io.File;
import java.io.FileOutputStream;

import org.apache.commons.io.IOUtils;
import org.apache.http.Header;
import org.springframework.util.Assert;

import com.alibaba.fastjson.JSONObject;

import cn.wuxia.project.common.third.baidu.OpenAccount;
import cn.wuxia.common.util.FileUtil;
import cn.wuxia.common.util.StringUtil;
import cn.wuxia.common.web.httpclient.HttpClientRequest;
import cn.wuxia.common.web.httpclient.HttpClientResponse;

public class VoiceCompound {


    private static final String ttsURL = "http://tsn.baidu.com/text2audio";


    private OpenAccount account;

    /**
     * 用户唯一标识，用来区分用户，计算UV值。建议填写能区分用户的机器 MAC 地址或 IMEI 码
     */
    private String cuid = StringUtil.random(10);

    /**
     * 选填  语速，取值0-9，默认为5中语速
     */
    private int speed = 5;

    /**
     *  选填  音调，取值0-9，默认为5中语调
     */
    private int pit = 5;

    /**
     *  选填  音量，取值0-15，默认为5中音量
     */
    private int vol = 5;

    /**
     * 选填  发音人选择, 0为普通女声，1为普通男生，3为情感合成-度逍遥，4为情感合成-度丫丫，默认为普通女声
     */
    private int per = 0;

    public VoiceCompound(OpenAccount openAccount) {
        this.account = openAccount;
    }

    public static VoiceCompound build(OpenAccount openAccount) {
        return new VoiceCompound(openAccount);
    }

    /**
     * 用户唯一标识，用来区分用户，计算UV值。建议填写能区分用户的机器 MAC 地址或 IMEI 码
     */
    public VoiceCompound cuid(String cuid) {
        if (StringUtil.isNotBlank(cuid)) {
            this.cuid = cuid;
        }
        return this;
    }

    /**
     * 选填  语速，取值0-9，默认为5中语速
     */
    public VoiceCompound speed(int speed) {
        if (0 < speed && speed <= 9)
            this.speed = speed;
        return this;
    }

    /**
     *  选填  音调，取值0-9，默认为5中语调
     */
    public VoiceCompound pit(int pit) {
        if (0 < pit && pit <= 9)
            this.pit = pit;
        return this;
    }

    /**
     *  选填  音量，取值0-15，默认为5中音量
     */
    public VoiceCompound vol(int vol) {
        if (0 < vol && vol <= 15)
            this.vol = vol;
        return this;
    }

    /**
     * 选填  发音人选择, 0为普通女声，1为普通男生，3为情感合成-度逍遥，4为情感合成-度丫丫，默认为普通女声
     */
    public VoiceCompound per(int per) {
        if (0 <= per && per <= 4 && per != 2)
            this.per = per;
        return this;
    }

    /**
     * 500  不支持输入
        501 输入参数不正确
        502 token验证失败
        503 合成后端错误
     */
    public File compound(String text) throws Exception {
        Assert.isTrue(text.length() < 512, "必须小于512个中文字或者英文数字");
        Assert.isTrue(0 <= speed && speed <= 9, "语速，取值0-9，默认为5中语速");
        Assert.isTrue(0 <= pit && pit <= 9, "音调，取值0-9，默认为5中语调");
        Assert.isTrue(0 <= vol && vol <= 15, "音量，取值0-15，默认为5中音量");
        Assert.isTrue(0 <= per && per <= 4 && per != 2, "发音人选择, 0为普通女声，1为普通男生，3为情感合成-度逍遥，4为情感合成-度丫丫");

        JSONObject params = new JSONObject();
        params.put("tex", text);
        params.put("lan", "zh");
        params.put("tok", account.getAccessToken());
        params.put("ctp", "1");
        params.put("cuid", cuid);
        params.put("spd", speed);
        params.put("pit", pit);
        params.put("vol", vol);
        params.put("per", per);

        HttpClientResponse resp = HttpClientRequest.post(ttsURL).setParam(params).execute();
        org.apache.http.Header[] headers = resp.getResponseHeaders();

        for (Header header : headers) {
            if (StringUtil.equalsIgnoreCase(header.getName(), "Content-type")) {
                if (StringUtil.equalsIgnoreCase("audio/mp3", header.getValue())) {
                    break;
                } else {
                    throw new RuntimeException(resp.getStringResult());
                }
            }
        }
        File mp3file = new File(System.getProperty("java.io.tmpdir") + File.separator + "baidutts" + File.separator + StringUtil.random(6) + ".mp3");
        FileUtil.forceMkdirParent(mp3file);
        FileOutputStream file = new FileOutputStream(mp3file);
        IOUtils.copy(resp.getContent(), file);
        IOUtils.closeQuietly(file);
        return mp3file;
    }
}
