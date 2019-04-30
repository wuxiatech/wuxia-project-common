/*
 * Created on :14 Sep, 2014
 * Author     :songlin
 * Change History
 * Version       Date         Author           Reason
 * <Ver.No>     <date>        <who modify>       <reason>
 * Copyright 2014-2020 www.ibmall.cn All right reserved.
 */
package cn.wuxia.project.common.bean;

import cn.wuxia.common.util.BytesUtil;
import cn.wuxia.common.util.EncodeUtils;
import cn.wuxia.common.util.JsonUtil;
import cn.wuxia.common.util.StringUtil;
import cn.wuxia.common.util.reflection.ReflectionUtil;
import cn.wuxia.project.common.support.CallbackBeanHandler;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.springframework.util.Assert;

import java.io.IOException;
import java.io.Serializable;
import java.util.Collection;

/**
 * handler @see {@link CallbackBeanHandler}
 *
 * @param
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CallbackBean implements Serializable {

    /**
     * Comment for <code>serialVersionUID</code>
     */
    private static final long serialVersionUID = 1L;

    /**
     * 默认值
     */
    private int status = MsgStatus.SUCCESSFUL.status;

    /**
     * 默认值
     */
    private String message = "成功";

    private CallbackResultType resultType;

    private String encodeResult;

    private Object data;

    private boolean needEncode = true;

    public static CallbackBean ok() {
        return new CallbackBean();
    }

    public static CallbackBean ok(Object object) throws IOException {
        if (object == null)
            return new CallbackBean();
        return new CallbackBean(object, false);
    }

    public static CallbackBean encode(Object object) throws IOException {
        if (object == null)
            return new CallbackBean();
        return new CallbackBean(object, true);
    }
    public static CallbackBean okJsonResult(Object object) throws IOException {
        return okJsonResult(object, true);
    }

    public static CallbackBean okJsonResult(Object object, boolean needEncode) throws IOException {
        if (object == null)
            return new CallbackBean();
        String json = JsonUtil.toJson(object);

        if (object instanceof Collection) {
            Class genricType = ReflectionUtil.getSuperClassGenricType(object.getClass());
            return new CallbackBean(json,
                    new CallbackResultType(CallbackResultType.MsgType.LIST_JSON, genricType != null ? genricType.getName() : ""), needEncode);
        } else
            return new CallbackBean(json, new CallbackResultType(CallbackResultType.MsgType.JSON, object.getClass().getName()), needEncode);
    }

    public static CallbackBean ok(byte[] content, String contentType) {
        return new CallbackBean(content, new CallbackResultType(CallbackResultType.MsgType.BYTES, contentType));
    }

    public static CallbackBean notok(String message) {
        return new CallbackBean(message, MsgStatus.SERVER_ERROR);
    }

    public CallbackBean() {
    }

    public CallbackBean(Object object, boolean needEncode) throws IOException {
        this();
        Assert.notNull(object, "");
        if(needEncode) {
            this.resultType = new CallbackResultType(CallbackResultType.MsgType.BYTES, object.getClass().getName());
            /**
             * 作为http传递必须使用带url方法
             */
            this.encodeResult = EncodeUtils.base64UrlSafeEncode(BytesUtil.objectToBytes(object));
        }else{
            this.resultType = new CallbackResultType(CallbackResultType.MsgType.JSON, object.getClass().getName());
            this.data = object;
        }
    }

    public CallbackBean(byte[] content, CallbackResultType resultType) {
        this.resultType = resultType;
        /**
         * 作为http传递必须使用带url方法
         */
        this.encodeResult = EncodeUtils.base64UrlSafeEncode(content);
    }

    public CallbackBean(String content, CallbackResultType resultType, boolean needEncode) {
        Assert.notNull(content, "");
        this.resultType = resultType;
        /**
         * 作为http传递必须使用带url方法
         */
        this.needEncode = needEncode;
        if (needEncode)
            this.encodeResult = EncodeUtils.base64UrlSafeEncode(content.getBytes());
        else this.encodeResult = content;
    }

    public CallbackBean(String text) {
        this(text, new CallbackResultType(CallbackResultType.MsgType.TEXT, ""), false);
    }


    public CallbackBean(String message, MsgStatus status) {
        this.message = message;
        this.status = status.status;
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

    public boolean getNeedEncode() {
        return needEncode;
    }

    public void setNeedEncode(boolean needEncode) {
        this.needEncode = needEncode;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    @JsonIgnore
    public byte[] getByteResult() {
        if (StringUtil.isNotBlank(this.encodeResult)) {
            if (this.needEncode)
                return EncodeUtils.base64Decode(this.encodeResult);
            else
                return this.encodeResult.getBytes();
        } else
            return null;
    }

    private void setEncodeResult(String encodeResult) {
        this.encodeResult = encodeResult;
    }

    public String getEncodeResult() {
        return encodeResult;
    }

    @JsonIgnore
    public String getStringResult() {
        if (resultType != null) {
            switch (resultType.msgType) {
                case BYTES:
                    break;
                case TEXT:
                    return encodeResult;
                case JSON:
                case LIST_JSON:
                    return new String(getByteResult());
            }
        }
        return null;
    }

    @JsonIgnore
    public <T> T getRtnObj() throws IOException, ClassNotFoundException {
        if (resultType != null) {
            switch (resultType.msgType) {
                case BYTES:
                    break;
                case TEXT:
                    return (T) encodeResult;
                case JSON:
                case LIST_JSON:
                    return (T) new String(getByteResult());
            }
        }
        return (T) BytesUtil.bytesToObject(getByteResult());
    }

    public CallbackResultType getResultType() {
        return resultType;
    }

    public void setResultType(CallbackResultType resultType) {
        this.resultType = resultType;
    }

    @JsonIgnore
    public boolean isok() {
        return getStatus() == MsgStatus.SUCCESSFUL.status;
    }

    public enum MsgStatus {
        INFORMATIONAL(1), SUCCESSFUL(2), REDIRECTION(3), CLIENT_ERROR(4), SERVER_ERROR(5);
        private int status;

        MsgStatus(int status) {
            this.status = status;
        }

        public int getStatus() {
            return status;
        }

        @Override
        public String toString() {
            return "" + status;
        }
    }

    @Override
    public String toString() {
        return "{status:" + status + ";message:" + message + ";resultType:" + resultType + ";result:base64UrlSafeEncode(****)}";
    }

}
