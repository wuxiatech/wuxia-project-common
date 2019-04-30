/*
 * Created on :2017年3月16日
 * Author     :songlin
 * Change History
 * Version       Date         Author           Reason
 * <Ver.No>     <date>        <who modify>       <reason>
 * Copyright 2014-2020 www.ibmall.cn All right reserved.
 */
package cn.wuxia.project.common.bean;

import cn.wuxia.common.util.JsonUtil;
import cn.wuxia.common.util.StringUtil;
import com.google.common.collect.Maps;

import java.io.Serializable;
import java.util.Map;

public class RequestBean implements Serializable {
    /**
     * Comment for <code>serialVersionUID</code>
     */
    private static final long serialVersionUID = -5684306963071293064L;

    String method;

    String version;

    Object params;

    public RequestBean() {
    }

    public RequestBean(String method, Object params, String version) {
        this.method = method;
        this.params = params;
        this.version = version;
    }

    public RequestBean(String method, String version) {
        this.method = method;
        this.version = version;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public Object getParams() {
        return params;
    }

    public void setParams(String params) {
        this.params = params;
    }

    public Map<String, String> toMap() {
        Map<String, String> reqBean = Maps.newLinkedHashMap();
        reqBean.put("method", method);
        String json = getJsonParams();
        if (StringUtil.isNotBlank(json)) {
            reqBean.put("params", json);
        } else {
            /**
             * 该参数不能为null
             */
            reqBean.put("params", "");
        }
        reqBean.put("version", version);
        return reqBean;
    }

    public String getJsonParams() {
        if (params == null) {
            return null;
        } else if (params instanceof String) {
            if (StringUtil.startsWith((String) params, "{") || StringUtil.startsWith((String) params, "[")) {
                return (String) params;
            } else {
                System.out.println("[警告：非json格式】-->" + params);
                return (String) params;
            }
        } else {
            return JsonUtil.toJson(params);
        }

    }

    @Override
    public String toString() {
        return String.format("{method=%s,version=%s,params=%s}", method, version, "加密数据");
    }
}
