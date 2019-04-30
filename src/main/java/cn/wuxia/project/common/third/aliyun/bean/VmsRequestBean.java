package cn.wuxia.project.common.third.aliyun.bean;

import cn.wuxia.common.entity.ValidationEntity;
import cn.wuxia.common.util.MapUtil;
import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Maps;
import org.hibernate.validator.constraints.NotBlank;

import java.util.Map;

public class VmsRequestBean extends ValidationEntity {
    //必填-被叫显号,可在语音控制台中找到所购买的显号
    @NotBlank
    String alledShowNumber;
    //必填-被叫号码
    @NotBlank
    String calledNumber;
    //必填-Tts模板ID
    @NotBlank
    String ttsCode;

    //可选-当模板中存在变量时需要设置此值
    Map<String, String> ttsParam;
    //可选-外部扩展字段,此ID将在回执消息中带回给调用方
    String outId;


    public String getAlledShowNumber() {
        return alledShowNumber;
    }

    public void setAlledShowNumber(String alledShowNumber) {
        this.alledShowNumber = alledShowNumber;
    }

    public String getCalledNumber() {
        return calledNumber;
    }

    public void setCalledNumber(String calledNumber) {
        this.calledNumber = calledNumber;
    }

    public String getTtsCode() {
        return ttsCode;
    }

    public void setTtsCode(String ttsCode) {
        this.ttsCode = ttsCode;
    }

    public Map<String, String> getTtsParam() {
        return ttsParam;
    }

    public void setTtsParam(Map<String, String> ttsParam) {
        this.ttsParam = ttsParam;
    }

    public void putParam(String key, String value) {
        if (MapUtil.isEmpty(ttsParam)) {
            ttsParam = Maps.newHashMap();
        }
        ttsParam.put(key, value);
    }

    public String toTtsParamJson(){
        if(MapUtil.isNotEmpty(ttsParam)) {
            return JSONObject.toJSONString(ttsParam);
        }
        return null;
    }

    public String getOutId() {
        return outId;
    }

    public void setOutId(String outId) {
        this.outId = outId;
    }
}
