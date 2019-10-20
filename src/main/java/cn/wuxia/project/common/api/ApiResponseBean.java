/*
 * Created on :14 Sep, 2014
 * Author     :songlin
 * Change History
 * Version       Date         Author           Reason
 * <Ver.No>     <date>        <who modify>       <reason>
 * Copyright 2014-2020 wuxia.tech All right reserved.
 */
package cn.wuxia.project.common.api;

import cn.wuxia.common.util.EncodeUtils;
import cn.wuxia.common.util.StringUtil;
import cn.wuxia.project.common.support.CallbackBeanHandler;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.SerializationUtils;

import java.io.Serializable;

/**
 * handler @see {@link CallbackBeanHandler}
 *
 * @param
 */
@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ApiResponseBean implements Serializable {
    /**
     * 默认值
     */
    private int status = MsgStatusEnum.SUCCESSFUL.getStatus();

    /**
     * 默认值
     */
    private String message = "成功";

    private RequestTypeEnum resultType;

    private String data;
    /**
     * Comment for <code>serialVersionUID</code>
     */
    private static final long serialVersionUID = 1L;


    @JsonIgnore
    public byte[] getBytes() {
        if (StringUtil.isNotBlank(getData())) {
            return EncodeUtils.base64Decode(getData());
        }
        return null;
    }


    @JsonIgnore
    public String getString() {
        if (getResultType() != null) {
            switch (getResultType().msgType) {
                case TEXT:
                case JSON:
                case LIST_JSON:
                    return new String(EncodeUtils.base64Decode(getData()));
                default:
                    break;
            }
        }
        return null;
    }

    @JsonIgnore
    public <T extends Serializable> T getObject() {
        if (getResultType() != null) {
            switch (getResultType().msgType) {
                case BYTES:
                    return (T) SerializationUtils.deserialize(getBytes());
                case TEXT:
                case JSON:
                case LIST_JSON:
                    return (T) new String(EncodeUtils.base64Decode(getData()));
                default:
                    break;
            }
        }
        return null;
    }


    @JsonIgnore
    public boolean isok() {
        return getStatus() == MsgStatusEnum.SUCCESSFUL.getStatus();
    }


    @Override
    public String toString() {
        return "{status:" + getString() + ";message:" + getMessage() + ";resultType:" + getResultType() + ";result:base64UrlSafeEncode(****)}";
    }

}
