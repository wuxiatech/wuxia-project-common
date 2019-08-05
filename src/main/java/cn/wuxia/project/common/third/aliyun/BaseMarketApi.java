/*
 * Created on :2017年7月14日
 * Author     :songlin
 * Change History
 * Version       Date         Author           Reason
 * <Ver.No>     <date>        <who modify>       <reason>
 * Copyright 2014-2020 wuxia.gd.cn All right reserved.
 */
package cn.wuxia.project.common.third.aliyun;

import cn.wuxia.aliyun.components.ApiAccount;
import cn.wuxia.aliyun.components.gateway.util.HttpUtils;
import cn.wuxia.common.util.StringUtil;
import cn.wuxia.common.web.httpclient.HttpAction;
import org.apache.http.HttpResponse;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

public class BaseMarketApi {
    protected final static Logger logger = LoggerFactory.getLogger(BaseMarketApi.class);

    protected String AppCode;

    protected ApiAccount apiAccount;

    public BaseMarketApi(String appCode) {
        AppCode = appCode;
    }

    public BaseMarketApi(ApiAccount apiAccount) {
        this.apiAccount = apiAccount;
//        this.AppCode = apiAccount.apiCode;
    }

    public String get(String url, Map<String, String> querys) {
        String[] host = StringUtil.split(url, "/", 3);
        String method = "GET";
        Map<String, String> headers = new HashMap<String, String>();
        //最后在header中的格式(中间是英文空格)为Authorization:APPCODE 83359fd73fe94948385f570e3c139105
        headers.put("Authorization", "APPCODE " + AppCode);
        try {
            HttpResponse response = HttpUtils.doGet(host[0] + "//" + host[1], "/" + host[2], method, headers, querys);
            return EntityUtils.toString(response.getEntity());
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return "";
    }

    public String send(HttpAction action, Map<String, String> querys) {
        return get(action.getUrl(), querys);
    }
//    public void post(String url, Map<String, String> querys) {
//        String[] host = StringUtil.split(url, "/", 3);
//        String method = "POST";
//        Map<String, String> headers = new HashMap<String, String>();
//        //最后在header中的格式(中间是英文空格)为Authorization:APPCODE 83359fd73fe94948385f570e3c139105
//        headers.put("Authorization", "APPCODE " + AppCode);
//        try {
//        } catch (Exception e) {
//            // TODO Auto-generated catch block
//            e.printStackTrace();
//        }
//    }
}
