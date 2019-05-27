/*
 * Created on :14 Sep, 2014
 * Author     :songlin
 * Change History
 * Version       Date         Author           Reason
 * <Ver.No>     <date>        <who modify>       <reason>
 * Copyright 2014-2020 www.ibmall.cn All right reserved.
 */
package cn.wuxia.project.common.api;

import cn.wuxia.common.util.EncodeUtils;
import cn.wuxia.common.util.JsonUtil;
import cn.wuxia.common.util.reflection.ReflectionUtil;
import cn.wuxia.project.common.support.CallbackBeanHandler;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.SerializationUtils;
import org.springframework.util.Assert;

import java.io.Serializable;
import java.util.Collection;

/**
 * handler @see {@link CallbackBeanHandler}
 *
 * @param
 */
@Getter
@Setter
@JsonInclude(JsonInclude.Include.ALWAYS)
public class ApiRequestBean extends ApiResponseBean implements Serializable {

    /**
     * Comment for <code>serialVersionUID</code>
     */
    private static final long serialVersionUID = 1L;


    public static ApiRequestBean ok() {
        return new ApiRequestBean();
    }

    public static ApiRequestBean ok(Serializable object) {
        return encode(object);
    }

    public static ApiRequestBean encode(Serializable object) {
        if (object == null) {
            return new ApiRequestBean();
        }
        if (object instanceof String) {
            return new ApiRequestBean((String) object, new RequestTypeEnum(RequestTypeEnum.MsgType.TEXT, ""));
        } else {
            return new ApiRequestBean(SerializationUtils.serialize(object), new RequestTypeEnum(RequestTypeEnum.MsgType.BYTES, ""));
        }
    }


    public static ApiRequestBean okJson(Serializable object) {
        if (object == null) {
            return new ApiRequestBean();
        }
        String json = JsonUtil.toJson(object);

        if (object instanceof Collection) {
            Class genricType = ReflectionUtil.getSuperClassGenricType(object.getClass());
            return new ApiRequestBean(json,
                    new RequestTypeEnum(RequestTypeEnum.MsgType.LIST_JSON, genricType != null ? genricType.getName() : ""));
        } else {
            return new ApiRequestBean(json, new RequestTypeEnum(RequestTypeEnum.MsgType.JSON, object.getClass().getName()));
        }
    }

    public static ApiRequestBean ok(byte[] content, String contentType) {
        return new ApiRequestBean(content, new RequestTypeEnum(RequestTypeEnum.MsgType.BYTES, contentType));
    }

    public static ApiRequestBean notok(String message) {
        return new ApiRequestBean(message, MsgStatusEnum.SERVER_ERROR);
    }

    public ApiRequestBean() {
    }


    public ApiRequestBean(byte[] content, RequestTypeEnum resultType) {
        setResultType(resultType);
        /**
         * 作为http传递必须使用带url方法
         */
        setData(EncodeUtils.base64UrlSafeEncode(content));
    }

    public ApiRequestBean(String content, RequestTypeEnum resultType) {
        Assert.notNull(content, "");
        setResultType(resultType);
        /**
         * 作为http传递必须使用带url方法
         */
        setData(EncodeUtils.base64UrlSafeEncode(content.getBytes()));

    }


    public ApiRequestBean(String message, MsgStatusEnum status) {
        setMessage(message);
        setStatus(status.getStatus());
    }


    @Override
    public String toString() {
        return "{status:" + getString() + ";message:" + getMessage() + ";resultType:" + getResultType() + ";result:base64UrlSafeEncode(****)}";
    }

}
