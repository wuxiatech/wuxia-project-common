/*
 * Created on :2017年7月14日
 * Author     :songlin
 * Change History
 * Version       Date         Author           Reason
 * <Ver.No>     <date>        <who modify>       <reason>
 * Copyright 2014-2020 wuxia.gd.cn All right reserved.
 */
package cn.wuxia.project.common.third.aliyun;

import cn.wuxia.aliyun.components.gateway.util.HttpUtils;
import cn.wuxia.common.util.StringUtil;
import org.apache.http.HttpResponse;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

public class ApiConstants {
    protected final static Logger logger = LoggerFactory.getLogger(ApiConstants.class);

    protected String AppCode = "b915a1e017d04bd48c5b6d64bc9ace7c";

    public ApiConstants() {
    }

    public ApiConstants(String appCode) {
        AppCode = appCode;
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

    public void post(String url, Map<String, String> querys) {
        String[] host = StringUtil.split(url, "/", 3);
        String method = "POST";
        Map<String, String> headers = new HashMap<String, String>();
        //最后在header中的格式(中间是英文空格)为Authorization:APPCODE 83359fd73fe94948385f570e3c139105
        headers.put("Authorization", "APPCODE " + AppCode);
        try {
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
}
