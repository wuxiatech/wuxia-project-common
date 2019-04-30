/*
* Created on :2017年6月5日
* Author     :songlin
* Change History
* Version       Date         Author           Reason
* <Ver.No>     <date>        <who modify>       <reason>
* Copyright 2014-2020 wuxia.gd.cn All right reserved.
*/
package cn.wuxia.project.common.third.fenci;

import cn.wuxia.common.util.JsonUtil;
import cn.wuxia.common.util.ListUtil;
import cn.wuxia.common.util.MapUtil;
import cn.wuxia.common.util.StringUtil;
import cn.wuxia.common.web.MediaTypes;
import cn.wuxia.common.web.httpclient.HttpClientException;
import cn.wuxia.common.web.httpclient.HttpClientRequest;
import cn.wuxia.common.web.httpclient.HttpClientResponse;
import cn.wuxia.common.web.httpclient.HttpClientUtil;
import cn.wuxia.project.common.third.fenci.bean.NerBean;
import org.apache.commons.collections4.MapUtils;
import org.apache.http.entity.ContentType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.Assert;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class BosonUtil {
    protected static Logger logger = LoggerFactory.getLogger(BosonUtil.class);

    static final String TOKEN = "hYmACLYP.15622.eNfuinMdaiQ3";

    /**
     * 命名实体，如时间，地点，人物
     */
    static final String ner = "http://api.bosonnlp.com/ner/analysis";

    /**
     * 分词
     */
    static final String keywords = "http://api.bosonnlp.com/keywords/analysis";

    /**
     * 词语联想
     */
    static final String suggest = "http://api.bosonnlp.com/suggest/analysis";

    /**
     * 时间转换
     */
    static final String time = "http://api.bosonnlp.com/time/analysis";

    /**
     * 命名实体，如时间，地点，人物
     */
    public static NerBean ner(String keyword) throws HttpClientException {
        Map<String, Object> map = post(ner, keyword);
        NerBean nerBean = new NerBean();
        nerBean.setEntity((ArrayList)MapUtils.getObject(map, "entity"));
        nerBean.setTag((ArrayList)MapUtils.getObject(map, "tag"));
        nerBean.setWord((ArrayList)MapUtils.getObject(map, "word"));
        return nerBean;
    }

    /**
     * 分词
     */
    public static List weight(String keyword) throws HttpClientException {
        return post(keywords, keyword);
    }

    /**
     * 词语联想
     */
    public static List suggest(String keyword) throws HttpClientException {
        return post(suggest, keyword);
    }

    /**
     * 时间转化
     */
    public static String time(String time) {
        Assert.notNull(time, "time不能为空");
        HttpClientRequest request = new HttpClientRequest(BosonUtil.time + "?pattern=" + time).addHeader("Content-Type",
                ContentType.create(MediaTypes.JSON).toString());
        request.addHeader("X-Token", TOKEN);
        HttpClientResponse resp;
        try {
            resp = HttpClientUtil.post(request);
        } catch (HttpClientException e) {
            throw new RuntimeException(e);
        }
        resp.setCharset("utf-8");
        String result = resp.getStringResult();

        Map map = JsonUtil.fromJson(result);
        if (MapUtil.isNotEmpty(map)) {
            String timestamp = MapUtil.getString(map, "timestamp");
            return timestamp;
        }
        return null;

    }

    private static <T> T post(String url, String keywords) throws HttpClientException {
        HttpClientRequest request = new HttpClientRequest(url);
        request.addHeader("X-Token", TOKEN);
        HttpClientResponse resp = HttpClientUtil.postJson(request, "[\"" + keywords + "\"]");

        resp.setCharset("utf-8");

        String result = resp.getStringResult();
        if (StringUtil.startsWith(result, "[")) {

            List list = JsonUtil.fromJson(result, List.class);
            if (ListUtil.isNotEmpty(list)) {
                return (T) list.get(0);
            }
        } else if (StringUtil.startsWith(result, "{")) {
            Map map = JsonUtil.fromJson(result);
            return (T) map;
        }

        return null;

    }

}
