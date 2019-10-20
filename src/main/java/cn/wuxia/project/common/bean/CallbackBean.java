/*
 * Created on :14 Sep, 2014
 * Author     :songlin
 * Change History
 * Version       Date         Author           Reason
 * <Ver.No>     <date>        <who modify>       <reason>
 * Copyright 2014-2020 wuxia.tech All right reserved.
 */
package cn.wuxia.project.common.bean;

import cn.wuxia.common.util.EncodeUtils;
import cn.wuxia.common.util.JsonUtil;
import cn.wuxia.common.util.StringUtil;
import cn.wuxia.common.util.reflection.ReflectionUtil;
import cn.wuxia.project.common.api.MsgStatusEnum;
import cn.wuxia.project.common.support.CallbackBeanHandler;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.apache.commons.lang3.SerializationUtils;
import org.springframework.util.Assert;

import java.io.IOException;
import java.io.Serializable;
import java.util.Collection;

/**
 * handler @see {@link CallbackBeanHandler}
 *
 * @param
 */
@Deprecated
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CallbackBean implements Serializable {

    /**
     * Comment for <code>serialVersionUID</code>
     */
    private static final long serialVersionUID = 1L;

    /**
     * 默认值
     */
    private int status = MsgStatusEnum.SUCCESSFUL.getStatus();

    /**
     * 默认值
     */
    private String message = "成功";

    private CallbackResultType resultType;

    private String data;

    public static CallbackBean ok() {
        return new CallbackBean();
    }

    public static CallbackBean ok(Serializable object) {
        return encode(object);
    }

    public static CallbackBean encode(Serializable object) {
        if (object == null) {
            return new CallbackBean();
        }
        if (object instanceof String) {
            return new CallbackBean((String) object, new CallbackResultType(CallbackResultType.MsgType.TEXT, ""));
        } else {
            return new CallbackBean(SerializationUtils.serialize(object), new CallbackResultType(CallbackResultType.MsgType.BYTES, ""));
        }
    }


    public static CallbackBean okJson(Serializable object) {
        if (object == null) {
            return new CallbackBean();
        }
        String json = JsonUtil.toJson(object);

        if (object instanceof Collection) {
            Class genricType = ReflectionUtil.getSuperClassGenricType(object.getClass());
            return new CallbackBean(json,
                    new CallbackResultType(CallbackResultType.MsgType.LIST_JSON, genricType != null ? genricType.getName() : ""));
        } else {
            return new CallbackBean(json, new CallbackResultType(CallbackResultType.MsgType.JSON, object.getClass().getName()));
        }
    }

    public static CallbackBean ok(byte[] content, String contentType) {
        return new CallbackBean(content, new CallbackResultType(CallbackResultType.MsgType.BYTES, contentType));
    }

    public static CallbackBean notok(String message) {
        return new CallbackBean(message, MsgStatusEnum.SERVER_ERROR);
    }

    public CallbackBean() {
    }


    public CallbackBean(byte[] content, CallbackResultType resultType) {
        this.resultType = resultType;
        /**
         * 作为http传递必须使用带url方法
         */
        this.data = EncodeUtils.base64UrlSafeEncode(content);
    }

    public CallbackBean(String content, CallbackResultType resultType) {
        Assert.notNull(content, "");
        this.resultType = resultType;
        /**
         * 作为http传递必须使用带url方法
         */
        this.data = EncodeUtils.base64UrlSafeEncode(content.getBytes());

    }


    public CallbackBean(String message, MsgStatusEnum status) {
        this.message = message;
        this.status = status.getStatus();
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }


    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    @JsonIgnore
    public byte[] getByteResult() {
        if (StringUtil.isNotBlank(this.data)) {
            return EncodeUtils.base64Decode(this.data);
        }
        return null;
    }


    @JsonIgnore
    public String getStringResult() {
        if (resultType != null) {
            switch (resultType.getMsgType()) {
                case TEXT:
                case JSON:
                case LIST_JSON:
                    return new String(EncodeUtils.base64Decode(data));
                default:
                    break;
            }
        }
        return null;
    }

    @JsonIgnore
    public <T extends Serializable> T getRtnObj() throws IOException, ClassNotFoundException {
        if (resultType != null) {
            switch (resultType.getMsgType()) {
                case BYTES:
                    return (T) SerializationUtils.deserialize(getByteResult());
                default:
                    break;
            }
        }
        return null;
    }

    public CallbackResultType getResultType() {
        return resultType;
    }

    public void setResultType(CallbackResultType resultType) {
        this.resultType = resultType;
    }

    @JsonIgnore
    public boolean isok() {
        return getStatus() == MsgStatusEnum.SUCCESSFUL.getStatus();
    }


    @Override
    public String toString() {
        return "{status:" + status + ";message:" + message + ";resultType:" + resultType + ";result:base64UrlSafeEncode(****)}";
    }

}
