/*
* Created on :2017年3月17日
* Author     :songlin
* Change History
* Version       Date         Author           Reason
* <Ver.No>     <date>        <who modify>       <reason>
* Copyright 2014-2020 www.ibmall.cn All right reserved.
*/
package cn.wuxia.project.common.third.yuncall.util;

import cn.wuxia.common.web.httpclient.HttpClientMethod;

public enum CallApiMethodEnum {

    /**
     * 主动挂机
     */
    API_CALL_HANGUP("/call/hangup/${ACCOUNTID}?sig=${SIGN}", HttpClientMethod.POST),

    API_CALL_DIALOUT("/call/dialout/${ACCOUNTID}?sig=${SIGN}", HttpClientMethod.POST),

    EVENT_WEBCALL("/command?Action=Webcall&Account=${ACCOUNTID}&PBX=gd.dx.3.5&Timeout=20&ServiceNo=${SERVICENO}&Exten=${mobile}", HttpClientMethod.GET);


    private String url;

    private HttpClientMethod method;

    CallApiMethodEnum(String url, HttpClientMethod method) {
        this.url = url;
        this.method = method;
    }

    public String getUrl() {
        return url;
    }

    public HttpClientMethod getMethod() {
        return method;
    }

}
